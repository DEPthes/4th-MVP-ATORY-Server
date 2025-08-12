package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.GoogleLoginRequestDto;
import ATORY.atory.domain.user.dto.GoogleLoginResponseDto;
import ATORY.atory.domain.user.service.UserService;
import ATORY.atory.global.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/google")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/callback")
    public ApiResult<GoogleLoginResponseDto> googleCallback(@RequestBody GoogleLoginRequestDto req) {
        return ApiResult.ok(userService.googleLogin(req.getCode()));
    }

}