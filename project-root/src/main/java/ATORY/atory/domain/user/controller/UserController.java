package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.GoogleLoginRequestDto;
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
import ATORY.atory.domain.user.service.UserService;
import ATORY.atory.global.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/google-login")
    public ResponseEntity<GoogleLoginResponseDto> googleLogin(@RequestBody GoogleLoginRequestDto requestDto) {
        GoogleLoginResponseDto responseDto = userService.googleLogin(requestDto.getCode());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/users/social/google/login")
    public ResponseEntity<SocialLoginResponseDto> socialGoogleLogin(@RequestBody SocialLoginRequestDto requestDto) {
        SocialLoginResponseDto responseDto = userService.socialLogin(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/users/profile/setup")
    public ResponseEntity<ProfileSetupResponseDto> setupProfile(@RequestBody ProfileSetupRequestDto requestDto) {
        ProfileSetupResponseDto responseDto = userService.setupProfile(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/business/validate")
    @Operation(summary = "사업자 등록번호 유효성 검증", description = "사업자 등록번호의 유효성을 검증합니다.")
    public ResponseEntity<BusinessValidationResponseDto> validateBusiness(@RequestBody BusinessValidationRequestDto requestDto) {
        BusinessValidationResponseDto responseDto = userService.validateBusiness(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/user/gnb-info")
    @Operation(summary = "GNB 사용자 정보 조회", description = "GNB에서 사용할 로그인 사용자 정보를 조회합니다.")
    public ResponseEntity<GnbUserInfoResponseDto> getGnbUserInfo(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        GnbUserInfoResponseDto responseDto = userService.getGnbUserInfo(userId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/auth/logout")
    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 처리합니다.")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        userService.logout(userId);
        return ResponseEntity.ok().build();
    }

    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtProvider.validateToken(token)) {
                return Long.parseLong(jwtProvider.getUserIdFromToken(token));
            }
        }
        return null;
    }
}