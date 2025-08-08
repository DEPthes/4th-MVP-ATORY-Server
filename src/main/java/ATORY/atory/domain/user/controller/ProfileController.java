package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.UserProfileResponseDto;
import ATORY.atory.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User Profile", description = "사용자 프로필 관련 API")
public class ProfileController {

    private final UserService userService;

    @GetMapping("/{userId}/profile")
    @Operation(summary = "사용자 프로필 조회", description = "특정 사용자의 프로필 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<UserProfileResponseDto> getUserProfile(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId) {
        
        UserProfileResponseDto response = userService.getUserProfile(userId);
        return ResponseEntity.ok(response);
    }
} 