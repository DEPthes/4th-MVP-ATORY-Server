package ATORY.atory.domain.gallery.controller;

import ATORY.atory.domain.gallery.dto.SmsDtos;
import ATORY.atory.domain.gallery.service.MessageService;
import ATORY.atory.domain.user.service.UserRegisterService;
import ATORY.atory.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final UserRegisterService userRegisterService;
    private final MessageService messageService;

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

    @Operation(summary = "문자 발송", description = "입력된 번호로 문자 발송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/register/send")
    public ApiResult<?> send(@RequestBody SmsDtos.SendRequest req) {

        if (req.getPhoneNumber() == null || req.getPhoneNumber().isBlank())
            return ApiResult.ok("오류");

        messageService.sendVerificationCode(req.getPhoneNumber());

        return ApiResult.ok("문자 발송 완료");
    }

    @Operation(summary = "문자 번호 인증", description = "입력된 번호로 발송된 번호 인증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/register/verify")
    public ApiResult<?> verify(@RequestBody SmsDtos.VerifyRequest req) {
        boolean ok = messageService.verifyCode(req.getPhoneNumber(), req.getCode());
        if (ok) return ApiResult.ok("인증 성공");

        return ApiResult.ok("인증 실패");
    }
}
