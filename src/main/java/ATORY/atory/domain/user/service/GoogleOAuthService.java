package ATORY.atory.domain.user.service;

import ATORY.atory.domain.user.dto.GoogleOAuthCallbackRequest;
import ATORY.atory.domain.user.dto.GoogleTokenResponse;
import ATORY.atory.domain.user.dto.GoogleUserInfo;
import ATORY.atory.domain.user.dto.GoogleLoginResponseDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.global.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.pw}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    @Transactional
    public GoogleLoginResponseDto processGoogleOAuthCallback(GoogleOAuthCallbackRequest request) {
        try {
            // 1. Authorization code로 access token 요청
            GoogleTokenResponse tokenResponse = getAccessToken(request.getCode());
            
            // 2. Access token으로 사용자 정보 요청
            GoogleUserInfo userInfo = getUserInfo(tokenResponse.getAccessToken());
            
            // 3. 사용자 정보로 DB에서 사용자 조회 또는 생성
            User user = findOrCreateUser(userInfo);
            
            // 4. JWT 토큰 생성
            String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail());
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail());
            
            // 5. 응답 생성
            return GoogleLoginResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(3600L) // 1시간
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .picture(user.getProfileImgUrl())
                    .provider(user.getProvider())
                    .isNewUser(user.getGoogleID() == null) // 새로 생성된 사용자인지 확인
                    .build();
                    
        } catch (HttpClientErrorException e) {
            log.error("Google OAuth API 호출 실패: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            
            // Google OAuth 정책 위반 등의 경우 더 구체적인 에러 메시지
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String errorBody = e.getResponseBodyAsString();
                if (errorBody.contains("OAuth 2.0 policy")) {
                    throw new RuntimeException("Google OAuth 정책 위반: Google Cloud Console에서 OAuth 설정을 확인해주세요.", e);
                } else if (errorBody.contains("invalid_request")) {
                    throw new RuntimeException("잘못된 OAuth 요청: authorization code가 유효하지 않습니다.", e);
                } else {
                    throw new RuntimeException("Google OAuth 인증 실패: " + e.getResponseBodyAsString(), e);
                }
            } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new RuntimeException("Google OAuth 인증 실패: 클라이언트 ID 또는 시크릿이 잘못되었습니다.", e);
            } else {
                throw new RuntimeException("Google OAuth API 호출 실패: " + e.getStatusCode(), e);
            }
        } catch (Exception e) {
            log.error("Google OAuth 처리 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("Google OAuth 인증 처리 중 오류가 발생했습니다.", e);
        }
    }

    private GoogleTokenResponse getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = String.format(
                "client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code&redirect_uri=%s",
                clientId, clientSecret, code, redirectUri
        );

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(
                GOOGLE_TOKEN_URL, request, GoogleTokenResponse.class
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Google access token 요청 실패");
        }

        return response.getBody();
    }

    private GoogleUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        
        ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(
                GOOGLE_USER_INFO_URL, HttpMethod.GET, request, GoogleUserInfo.class
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Google 사용자 정보 요청 실패");
        }

        return response.getBody();
    }

    private User findOrCreateUser(GoogleUserInfo googleUserInfo) {
        // Google ID로 사용자 조회
        Optional<User> existingUser = userRepository.findByGoogleID(googleUserInfo.getId());
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // 기존 사용자 정보 업데이트
            user.updateSocialInfo(googleUserInfo.getId(), "GOOGLE");
            if (googleUserInfo.getPicture() != null) {
                user.updateProfileImgUrl(googleUserInfo.getPicture());
            }
            return userRepository.save(user);
        }

        // 이메일로도 조회해보기 (기존 사용자가 Google 로그인을 처음 시도하는 경우)
        Optional<User> userByEmail = userRepository.findByEmail(googleUserInfo.getEmail());
        if (userByEmail.isPresent()) {
            User user = userByEmail.get();
            user.updateSocialInfo(googleUserInfo.getId(), "GOOGLE");
            if (googleUserInfo.getPicture() != null) {
                user.updateProfileImgUrl(googleUserInfo.getPicture());
            }
            return userRepository.save(user);
        }

        // 새 사용자 생성
        User newUser = User.createUser(
                googleUserInfo.getName(),
                googleUserInfo.getEmail(),
                googleUserInfo.getId(),
                "GOOGLE"
        );
        
        if (googleUserInfo.getPicture() != null) {
            newUser.updateProfileImgUrl(googleUserInfo.getPicture());
        }

        return userRepository.save(newUser);
    }
} 