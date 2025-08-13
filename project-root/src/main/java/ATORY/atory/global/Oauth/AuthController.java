package ATORY.atory.global.Oauth;

import ATORY.atory.global.Oauth.dto.AuthCodeRequestDto;
import ATORY.atory.global.Oauth.dto.LoginResponseDto;
import ATORY.atory.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "구글 로그인", description = "프론트에서 받은 auth code를 통해 Google 토큰 교환 후 id_token 디코딩 후 기존 유저 여부 판별")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/login")
    public ApiResult<LoginResponseDto> login(@RequestBody AuthCodeRequestDto authCodeRequestDto){
        String code = authCodeRequestDto.getCode();

        return ApiResult.ok(authService.login(code));
    }
}
