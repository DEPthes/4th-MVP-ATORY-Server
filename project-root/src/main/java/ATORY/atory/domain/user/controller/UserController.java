package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.GoogleLoginRequestDto;
import ATORY.atory.domain.user.dto.GoogleLoginResponseDto;
import ATORY.atory.domain.user.dto.SocialLoginRequestDto;
import ATORY.atory.domain.user.dto.SocialLoginResponseDto;
import ATORY.atory.domain.user.dto.ProfileSetupRequestDto;
import ATORY.atory.domain.user.dto.ProfileSetupResponseDto;
import ATORY.atory.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

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
}