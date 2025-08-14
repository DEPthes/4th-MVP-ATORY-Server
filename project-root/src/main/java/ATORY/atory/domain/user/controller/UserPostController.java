package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.UserWithPostDto;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.user.dto.CustomUserDetails;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.service.UserPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user_detail")
@RequiredArgsConstructor
@Tag(name = "유저", description = "유저관련 API")
public class UserPostController {
    
    private final UserPostService userPostService;
    

    @Operation(
            summary = "유저 게시물 조회", description = "유저 게시물을 조회합니다." ,
            parameters = {
                    @Parameter(name = "id", description = "유저 ID", example = "1"),
                    @Parameter(name = "type", description = "게시물 타입", example = "ART"),
                    @Parameter(name = "tag", description = "검색할 태그명", example = "watercolor"),
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
                    @Parameter(name = "size", description = "한 페이지 게시물 개수", example = "9")
            }
    )
    @GetMapping("/{id}/posts")
    public UserWithPostDto findPostsByIdAndTag(@PathVariable Long id, @RequestParam(value = "type") PostType postType,
                                               @RequestParam(value = "tag",required = false) String tag, Pageable pageable,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return userPostService.getPostByIdAndTag(id,postType,tag,pageable,customUserDetails);

    }

    @Operation(
            summary = "유저 아카이브 조회", description = "유저 아카이브를 조회합니다." ,
            parameters = {
                    @Parameter(name = "id", description = "작가 ID", example = "1"),
                    @Parameter(name = "type", description = "게시물 타입", example = "ART"),
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
                    @Parameter(name = "size", description = "한 페이지 게시물 개수", example = "9")
            }
    )
    @GetMapping("/{id}/archives")
    public UserWithPostDto findArchivesByIdAndType(@PathVariable Long id,
                                                   @RequestParam(value = "type",required = false) PostType postType, Pageable pageable,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userPostService.getArchiveByIdAndType(id,postType,pageable,customUserDetails);

    }
}
