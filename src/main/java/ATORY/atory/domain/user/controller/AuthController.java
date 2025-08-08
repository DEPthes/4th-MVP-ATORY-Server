package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.AuthMeResponseDto;
import ATORY.atory.domain.user.service.UserService;
import ATORY.atory.global.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "인증 관련 API")
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @GetMapping("/me")
    @Operation(summary = "로그인 상태 확인", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 상태"),
        @ApiResponse(responseCode = "401", description = "로그인되지 않음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<AuthMeResponseDto> getCurrentUser(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        
        if (token == null || !jwtProvider.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        
        Long userId = Long.parseLong(jwtProvider.getUserIdFromToken(token));
        AuthMeResponseDto response = userService.getCurrentUserInfo(userId);
        
        return ResponseEntity.ok(response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 