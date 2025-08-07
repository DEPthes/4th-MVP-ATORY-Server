package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.GoogleOAuthCallbackRequest;
import ATORY.atory.domain.user.dto.GoogleTokenResponse;
import ATORY.atory.domain.user.dto.GoogleUserInfo;
import ATORY.atory.domain.user.dto.GoogleLoginResponseDto;
import ATORY.atory.domain.user.service.GoogleOAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class LoginController {

    private final GoogleOAuthService googleOAuthService;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.pw}")
    private String googleClientPw;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    // [1단계] Google 로그인 URL 생성
    @GetMapping("/api/v1/oauth2/google")
    public String loginUrlGoogle() {
        String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + googleClientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=openid%20email%20profile"
                + "&access_type=offline"
                + "&prompt=consent";
        return reqUrl;
    }

    // [2단계] Authorization Code 콜백 처리 → 토큰 요청 → 사용자 정보 추출
    @PostMapping("/api/v1/oauth2/google/callback")
    public ResponseEntity<GoogleLoginResponseDto> googleOAuthCallback(@RequestBody GoogleOAuthCallbackRequest request) {
        try {
            log.info("Google OAuth callback received with code: {}", request.getCode());
            GoogleLoginResponseDto response = googleOAuthService.processGoogleOAuthCallback(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Google OAuth callback 처리 중 오류 발생", e);
            throw new RuntimeException("Google OAuth 인증 처리 중 오류가 발생했습니다.", e);
        }
    }
}
