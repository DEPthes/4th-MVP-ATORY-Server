package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.ProfileSetupRequestDto;
import ATORY.atory.domain.user.dto.ProfileSetupResponseDto;
import ATORY.atory.domain.user.service.UserService;
import ATORY.atory.global.dto.ApiResult;
import ATORY.atory.global.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileSetupController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/profile/setup")
    public ApiResult<ProfileSetupResponseDto> setupProfile(
            HttpServletRequest request,
            @RequestBody ProfileSetupRequestDto dto
    ) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new IllegalStateException("Authorization Bearer 토큰이 필요합니다.");
        }
        String token = auth.substring(7).trim();
        if (!jwtProvider.validateToken(token)) {
            throw new IllegalStateException("JWT 토큰이 유효하지 않습니다.");
        }
        Long tokenUserId = Long.parseLong(jwtProvider.getUserIdFromToken(token));
        if (!tokenUserId.equals(dto.getUserId())) {
            throw new IllegalStateException("요청 userId와 토큰의 userId가 일치하지 않습니다.");
        }

        return ApiResult.ok(userService.setupProfile(dto));
    }
}