package ATORY.atory.domain.archive.controller;

import ATORY.atory.domain.archive.service.ArchiveService;
import ATORY.atory.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/archive")
public class ArchiveController {

    private final ArchiveService archiveService;

    @Operation(summary = "아카이브 여부 변환", description = "return 값 false는 아카이브 취소, true는 아카이브 실행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("")
    public ApiResult<Boolean> toggleArchive(@RequestParam Long postId, @RequestParam String googleID){
        return ApiResult.ok(archiveService.switchArchive(postId, googleID));
    }
}
