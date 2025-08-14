package ATORY.atory.domain.gallery.controller;

import ATORY.atory.domain.user.service.UserRegisterService;
import ATORY.atory.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final UserRegisterService userRegisterService;

    @Operation(summary = "갤러리 사업자 조회", description = "사업자가 맞을경우 true, 아니면 false")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/register/artist")
    public ApiResult<Boolean> checkBusinessNum(@RequestParam String businessNum) {

        Boolean result = userRegisterService.checkBusinessStatus(businessNum);

        return ApiResult.ok(result);
    }
}
