package ATORY.atory.domain.post.controller;

import ATORY.atory.domain.post.dto.PostSaveDto;
import ATORY.atory.domain.post.service.PostService;
import ATORY.atory.global.dto.ApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/post")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시물 저장", description = "게시물 타입에 따라서 분류 저장됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/upload")
    public ApiResult<Boolean> save(@RequestBody PostSaveDto postSaveDto, @RequestParam String googleID) throws JsonProcessingException {
        return ApiResult.ok(postService.savePost(postSaveDto, googleID));
    }
}
