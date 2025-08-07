package ATORY.atory.domain.user.service;

import ATORY.atory.domain.user.dto.GoogleLoginResponseDto;
import ATORY.atory.domain.user.dto.SocialLoginRequestDto;
import ATORY.atory.domain.user.dto.SocialLoginResponseDto;
import ATORY.atory.domain.user.dto.ProfileSetupRequestDto;
import ATORY.atory.domain.user.dto.ProfileSetupResponseDto;
import ATORY.atory.domain.user.dto.BusinessValidationRequestDto;
import ATORY.atory.domain.user.dto.BusinessValidationResponseDto;

import ATORY.atory.domain.user.dto.GalleryProfileRequestDto;
import ATORY.atory.domain.user.dto.GalleryProfileResponseDto;
import ATORY.atory.domain.user.dto.ProfileUpdateRequestDto;
import ATORY.atory.domain.user.dto.GnbUserInfoResponseDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UseRepository;
import ATORY.atory.domain.artist.entity.YouthArtist;
import ATORY.atory.domain.artist.repository.YouthArtistRepository;
import ATORY.atory.domain.collector.entity.ArtCollector;
import ATORY.atory.domain.collector.repository.ArtCollectorRepository;
import ATORY.atory.domain.gallery.entity.Gallery;
import ATORY.atory.domain.gallery.repository.GalleryRepository;
import ATORY.atory.global.exception.UserNotFoundException;
import ATORY.atory.global.exception.InvalidRoleException;
import ATORY.atory.global.exception.BusinessValidationException;
import ATORY.atory.global.security.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UseRepository userRepository;
    private final YouthArtistRepository youthArtistRepository;
    private final ArtCollectorRepository artCollectorRepository;
    private final GalleryRepository galleryRepository;
    private final BusinessValidationService businessValidationService;
    private final JwtProvider jwtProvider;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.pw}")
    private String googleClientSecret;

    @Value("${google.redirect.uri}")
    private String googleRedirectUri;

    public GoogleLoginResponseDto googleLogin(String code) {
        try {
            if (code == null || code.trim().isEmpty()) {
                throw new IllegalArgumentException("인증 코드가 제공되지 않았습니다.");
            }

            String accessToken = getAccessToken(code);
            JSONObject userInfo = getUserInfo(accessToken);

            // 필수 필드 확인
            if (!userInfo.has("sub") || !userInfo.has("email") || !userInfo.has("name")) {
                throw new RuntimeException("Google 사용자 정보가 올바르지 않습니다.");
            }

            String googleId = userInfo.getString("sub");
            String email = userInfo.getString("email");
            String name = userInfo.getString("name");

            User user = userRepository.findByGoogleID(googleId)
                    .orElseGet(() -> {
                        User newUser = User.createUser(name, email, googleId, "google");
                        return userRepository.save(newUser);
                    });

            String token = jwtProvider.createToken(user.getId());
            return new GoogleLoginResponseDto(token, user.getEmail(), user.getUsername());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Google 로그인 처리 중 오류가 발생했습니다.", e);
        }
    }

    public SocialLoginResponseDto socialLogin(SocialLoginRequestDto requestDto) {
        // 기존 사용자 확인
        User user = userRepository.findByGoogleID(requestDto.getProviderId())
                .orElse(null);

        boolean isNewUser = false;
        
        if (user == null) {
            // 신규 사용자인 경우 회원가입 처리
            isNewUser = true;
            user = User.createUserWithProfile(
                    requestDto.getNickname(),
                    requestDto.getEmail(),
                    requestDto.getProviderId(),
                    requestDto.getSocialType().toLowerCase(),
                    null,
                    null,
                    requestDto.getProfileImgUrl(),
                    null
            );
            user = userRepository.save(user);
        } else {
            // 기존 사용자이지만 프로필 이미지가 업데이트된 경우
            if (user.getProfileImgUrl() == null || !user.getProfileImgUrl().equals(requestDto.getProfileImgUrl())) {
                user.updateProfileImgUrl(requestDto.getProfileImgUrl());
                userRepository.save(user);
            }
        }

        String token = jwtProvider.createToken(user.getId());

        return new SocialLoginResponseDto(
                token,
                user.getEmail(),
                user.getUsername(),
                user.getProfileImgUrl(),
                isNewUser
        );
    }

    public ProfileSetupResponseDto setupProfile(ProfileSetupRequestDto requestDto) {
        // 사용자 조회
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDto.getUserId()));

        // 사용자 기본 정보 업데이트
        user.updateProfileInfo(
                requestDto.getName(),
                requestDto.getPhone(),
                requestDto.getEmail(),
                requestDto.getBio(),
                null,
                null
        );
        
        // 프로필 완료 처리
        user.completeProfile();
        userRepository.save(user);

        // 역할에 따라 YouthArtist 또는 ArtCollector 엔티티 생성
        if ("ARTIST".equals(requestDto.getRole())) {
            YouthArtist youthArtist = YouthArtist.createYouthArtist(
                    user,
                    LocalDate.parse(requestDto.getBirthDate()),
                    requestDto.getEducation(),
                    requestDto.getIsEducationPublic()
            );
            youthArtistRepository.save(youthArtist);
        } else if ("COLLECTOR".equals(requestDto.getRole())) {
            ArtCollector artCollector = ArtCollector.createArtCollector(
                    user,
                    LocalDate.parse(requestDto.getBirthDate()),
                    requestDto.getEducation(),
                    requestDto.getIsEducationPublic()
            );
            artCollectorRepository.save(artCollector);
        } else {
            throw new InvalidRoleException("Invalid role: " + requestDto.getRole() + ". Must be 'ARTIST' or 'COLLECTOR'");
        }

        return new ProfileSetupResponseDto(
                user.getId(),
                requestDto.getRole(),
                user.getUsername(),
                "Profile setup completed successfully"
        );
    }

    public BusinessValidationResponseDto validateBusiness(BusinessValidationRequestDto requestDto) {
        try {
            // 필수 입력값 검증
            if (requestDto.getRegistrationNumber() == null || requestDto.getRegistrationNumber().trim().isEmpty()) {
                throw new BusinessValidationException("필수 정보가 누락되었습니다.");
            }

            boolean isValid = businessValidationService.validateBusinessRegistrationNumber(requestDto.getRegistrationNumber());
            String businessName = null;
            
            if (isValid) {
                businessName = businessValidationService.getBusinessName(requestDto.getRegistrationNumber());
            } else {
                throw new BusinessValidationException("유효하지 않은 사업자 등록번호입니다.");
            }
            
            return new BusinessValidationResponseDto(isValid, businessName);
            
        } catch (BusinessValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("사업자 등록번호 검증 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("회원가입에 실패했습니다. 잠시 후 다시 시도해주세요.", e);
        }
    }

    public GalleryProfileResponseDto setupGalleryProfile(GalleryProfileRequestDto requestDto) {
        try {
            // 사용자 조회
            User user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDto.getUserId()));

            // 사업자 등록번호 검증
            if (requestDto.getRegistrationNumber() != null && !requestDto.getRegistrationNumber().trim().isEmpty()) {
                boolean isValidBusiness = businessValidationService.validateBusinessRegistrationNumber(requestDto.getRegistrationNumber());
                if (!isValidBusiness) {
                    throw new BusinessValidationException("유효하지 않은 사업자 등록번호입니다.");
                }
            }

            // 갤러리 정보 저장
            Gallery gallery = Gallery.createGallery(
                    user,
                    requestDto.getGalleryName(),
                    requestDto.getLocation(),
                    requestDto.getRegistrationNumber()
            );
            
            galleryRepository.save(gallery);

            // 사용자 프로필 완료 처리
            user.completeProfile();
            userRepository.save(user);

            return new GalleryProfileResponseDto(
                    user.getId(),
                    gallery.getName(),
                    "프로필 등록 완료"
            );
            
        } catch (BusinessValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("갤러리 프로필 설정 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("회원가입에 실패했습니다. 잠시 후 다시 시도해주세요.", e);
        }
    }

    public User updateUserProfile(Long userId, ProfileUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        user.updateProfileInfo(
                requestDto.getUsername(),
                requestDto.getPhone(),
                requestDto.getEmail(),
                requestDto.getIntroduction(),
                requestDto.getProfileImgUrl(),
                requestDto.getCoverImgUrl()
        );

        return userRepository.save(user);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public GnbUserInfoResponseDto getGnbUserInfo(Long userId) {
        User user = findById(userId);
        return new GnbUserInfoResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileImgUrl()
        );
    }

    public void logout(Long userId) {
        // 로그아웃 로직 (필요시 구현)
    }

    private String getAccessToken(String code) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // properties 파일에서 OAuth 정보 가져오기
            String clientId = googleClientId;
            String clientSecret = googleClientSecret;
            String redirectUri = googleRedirectUri;

            log.info("Google OAuth 요청 - Client ID: {}, Redirect URI: {}, Code: {}", clientId, redirectUri, code);

            String body = "code=" + code +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&redirect_uri=" + redirectUri +
                    "&grant_type=authorization_code";

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://oauth2.googleapis.com/token",
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JSONObject json = new JSONObject(response.getBody());
                if (json.has("access_token")) {
                    return json.getString("access_token");
                } else {
                    throw new RuntimeException("Google OAuth 인증에 실패했습니다.");
                }
            } else {
                // Google OAuth 오류 응답 처리
                if (response.getBody() != null) {
                    try {
                        JSONObject errorJson = new JSONObject(response.getBody());
                        if (errorJson.has("error")) {
                            String error = errorJson.getString("error");
                            String errorDescription = errorJson.optString("error_description", "");
                            
                            if ("invalid_grant".equals(error)) {
                                throw new IllegalArgumentException("인증 코드가 만료되었거나 이미 사용되었습니다. 새로운 인증 코드를 발급받아주세요.");
                            } else if ("invalid_client".equals(error)) {
                                throw new IllegalArgumentException("클라이언트 인증 정보가 올바르지 않습니다.");
                            } else {
                                throw new IllegalArgumentException("Google OAuth 오류: " + error + " - " + errorDescription);
                            }
                        }
                    } catch (Exception parseException) {
                        // JSON 파싱 실패 시 기본 오류 메시지
                    }
                }
                throw new RuntimeException("Google OAuth API 호출에 실패했습니다.");
            }
        } catch (IllegalArgumentException e) {
            throw e; // IllegalArgumentException은 그대로 전달
        } catch (Exception e) {
            throw new RuntimeException("Google 로그인 처리 중 오류가 발생했습니다.", e);
        }
    }

    private JSONObject getUserInfo(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<String> entity = new HttpEntity<>("", headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return new JSONObject(response.getBody());
            } else {
                log.error("Google UserInfo API 호출 실패: {}", response.getStatusCode());
                throw new RuntimeException("Google 사용자 정보 조회에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("Google 사용자 정보 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("Google 사용자 정보 조회 중 오류가 발생했습니다.", e);
        }
    }
} 