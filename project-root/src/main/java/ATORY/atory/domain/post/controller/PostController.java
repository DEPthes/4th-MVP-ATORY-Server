package ATORY.atory.domain.post.controller;

import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.dto.PostRegisterDto;
import ATORY.atory.domain.post.service.PostService;
import ATORY.atory.domain.user.dto.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/post")
@Tag(name = "Post", description = "작품(게시물) 관련 API")
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시물 등록", description = "로그인한 사용자가 게시물을 등록합니다.")
    @PostMapping
    public PostRegisterDto createPost(@RequestBody PostRegisterDto postRegisterDto, @RequestParam("file") List<MultipartFile> files, @AuthenticationPrincipal CustomUserDetails user) throws JsonProcessingException {
        return postService.createPost(postRegisterDto,files,user);
    }

}
