package ATORY.atory.domain.post.controller;

import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.dto.PostListDto;
import ATORY.atory.domain.post.dto.PostSaveDto;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.post.service.PostService;
import ATORY.atory.global.dto.ApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "게시물 조회", description = "메인페이지 게시물 조회 타입에 따라 조회 게시물 다름")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/main")
    public ApiResult<Page<PostListDto>> getMainPosts(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam String googleID,
                                                     @RequestParam(defaultValue = "ALL") String tagName,
                                                     @RequestParam PostType postType){

        Pageable pageable = PageRequest.of(page, size);

        Page<PostListDto> posts = postService.loadPosts(pageable, googleID, tagName, postType);

        return ApiResult.ok(posts);
    }

    @Operation(summary = "게시물 상세 페이지 조회", description = "게시물 조회 타입에 따라 조회 게시물 다름")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/detail")
    public ApiResult<PostDto> getPostDetail(@RequestParam Long postID, @RequestParam String googleID){
        return ApiResult.ok(postService.loadPost(postID, googleID));
    }

    @Operation(summary = "게시물 검색", description = "게시물 타입에 따라 검색 게시물 다름")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/search")
    public ApiResult<Page<PostListDto>> getSearchPosts(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam String googleID,
                                                     @RequestParam(defaultValue = "ALL") String tagName,
                                                     @RequestParam PostType postType,
                                                       @RequestParam String text
                                                       ){

        Pageable pageable = PageRequest.of(page, size);

        Page<PostListDto> posts = postService.searchPosts(pageable, googleID, tagName, postType, text);

        return ApiResult.ok(posts);
    }
}
