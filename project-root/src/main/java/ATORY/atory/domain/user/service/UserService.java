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
import ATORY.atory.domain.user.dto.GnbUserInfoResponseDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UseRepository;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.collector.entity.Collector;
import ATORY.atory.domain.collector.repository.CollectorRepository;
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
import org.json.JSONObject;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UseRepository userRepository;
    private final ArtistRepository artistRepository;
    private final CollectorRepository collectorRepository;
    private final GalleryRepository galleryRepository;
    private final BusinessValidationService businessValidationService;
    private final JwtProvider jwtProvider;

    public GoogleLoginResponseDto googleLogin(String code) {
        String accessToken = getAccessToken(code);
        JSONObject userInfo = getUserInfo(accessToken);

        String googleId = userInfo.getString("sub");
        String email = userInfo.getString("email");
        String name = userInfo.getString("name");

        User user = userRepository.findByGoogleID(googleId)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .username(name)
                            .email(email)
                            .googleID(googleId)
                            .isSocial(true)
                            .provider("google")
                            .phone(null)
                            .birthDate(null)
                            .isProfileCompleted(false)
                            .build();
                    return userRepository.save(newUser);
                });

        String token = jwtProvider.createToken(user.getId());
        return new GoogleLoginResponseDto(token, user.getEmail(), user.getUsername());
    }

    public SocialLoginResponseDto socialLogin(SocialLoginRequestDto requestDto) {
        User user = userRepository.findByGoogleID(requestDto.getProviderId())
                .orElse(null);

        boolean isNewUser = false;

        if (user == null) {
            isNewUser = true;
            user = User.builder()
                    .username(requestDto.getNickname())
                    .email(requestDto.getEmail())
                    .googleID(requestDto.getProviderId())
                    .isSocial(true)
                    .provider(requestDto.getSocialType().toLowerCase())
                    .profileImgUrl(requestDto.getProfileImgUrl())
                    .phone(null)
                    .birthDate(null)
                    .isProfileCompleted(false)
                    .build();
            user = userRepository.save(user);
        } else {
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
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDto.getUserId()));

        user.updateProfileInfo(
                requestDto.getName(),
                requestDto.getPhone(),
                requestDto.getEmail(),
                requestDto.getBirthDate(),
                requestDto.getBio()
        );

        user.completeProfile();
        userRepository.save(user);

        if ("ARTIST".equals(requestDto.getRole())) {
            Artist artist = Artist.builder()
                    .user(user)
                    .birth(requestDto.getBirthDate())
                    .educationBackground(requestDto.getEducation())
                    .disclosureStatus(requestDto.getIsEducationPublic())
                    .build();
            artistRepository.save(artist);
        } else if ("COLLECTOR".equals(requestDto.getRole())) {
            Collector collector = Collector.builder()
                    .user(user)
                    .birth(requestDto.getBirthDate())
                    .educationBackground(requestDto.getEducation())
                    .disclosureStatus(requestDto.getIsEducationPublic())
                    .build();
            collectorRepository.save(collector);
        } else {
            throw new InvalidRoleException("Invalid role: " + requestDto.getRole());
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
            User user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + requestDto.getUserId()));

            if (requestDto.getRegistrationNumber() != null && !requestDto.getRegistrationNumber().trim().isEmpty()) {
                boolean isValidBusiness = businessValidationService.validateBusinessRegistrationNumber(requestDto.getRegistrationNumber());
                if (!isValidBusiness) {
                    throw new BusinessValidationException("유효하지 않은 사업자 등록번호입니다.");
                }
            }

            Gallery gallery = Gallery.builder()
                    .user(user)
                    .name(requestDto.getGalleryName())
                    .registrationNumber(requestDto.getRegistrationNumber())
                    .location(requestDto.getLocation())
                    .managerPhone(requestDto.getManagerPhone())
                    .managerName(requestDto.getManagerName())
                    .email(requestDto.getEmail())
                    .bio(requestDto.getBio())
                    .address(requestDto.getAddress())
                    .zipCode(requestDto.getZipCode())
                    .city(requestDto.getCity())
                    .district(requestDto.getDistrict())
                    .neighborhood(requestDto.getNeighborhood())
                    .build();

            galleryRepository.save(gallery);

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

    public GnbUserInfoResponseDto getGnbUserInfo(Long userId) {
        if (userId == null) {
            return GnbUserInfoResponseDto.builder()
                    .isAuthenticated(false)
                    .message("로그인 후 이용해주세요")
                    .build();
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return GnbUserInfoResponseDto.builder()
                    .isAuthenticated(false)
                    .message("로그인 후 이용해주세요")
                    .build();
        }

        String profession = "사용자";
        if (artistRepository.findByUserId(userId).isPresent()) {
            profession = "작가";
        } else if (collectorRepository.findByUserId(userId).isPresent()) {
            profession = "컬렉터";
        } else if (galleryRepository.findByUserId(userId).isPresent()) {
            profession = "갤러리";
        }

        return GnbUserInfoResponseDto.builder()
                .isAuthenticated(true)
                .nickname(user.getUsername())
                .profession(profession)
                .profileImageUrl(user.getProfileImgUrl())
                .build();
    }

    public void logout(Long userId) {
        log.info("사용자 로그아웃: {}", userId);
        // TODO: Redis 기반 JWT 블랙리스트 처리
    }

    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "code=" + code +
                "&client_id=" + "YOUR_GOOGLE_CLIENT_ID" +
                "&client_secret=" + "YOUR_GOOGLE_CLIENT_SECRET" +
                "&redirect_uri=" + "YOUR_REDIRECT_URI" +
                "&grant_type=authorization_code";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                request,
                String.class
        );

        JSONObject json = new JSONObject(response.getBody());
        return json.getString("access_token");
    }

    private JSONObject getUserInfo(String accessToken) {
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

        return new JSONObject(response.getBody());
    }
}