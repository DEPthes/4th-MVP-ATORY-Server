package ATORY.atory.domain.tag.controller;

import ATORY.atory.domain.tag.dto.TagDto;
import ATORY.atory.domain.tag.service.TagService;
import ATORY.atory.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 정보 조회", description = "태그 정조 전부 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/list")
    public ApiResult<List<TagDto>> getListTags() {
        return ApiResult.ok(tagService.loadTags());
    }
}
