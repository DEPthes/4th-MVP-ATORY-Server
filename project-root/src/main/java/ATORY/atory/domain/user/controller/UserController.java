package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.CustomUserDetails;
import ATORY.atory.domain.user.dto.UserDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
@Tag(name = "artist", description = "유저관련 API")
public class UserController {
    private final UserService userService;


    @Operation(
            summary = "유저 정보 조회", description = "유저 정보를 조회합니다.",
            parameters = {
                    @Parameter(name = "id", description = "작가 ID", example = "1")
            }
    )
    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.getUserById(id,customUserDetails);
    }
}
