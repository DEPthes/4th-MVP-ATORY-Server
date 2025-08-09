package ATORY.atory.domain.artist.controller;

import ATORY.atory.domain.artist.dto.ArtistWithArtistNoteDto;
import ATORY.atory.domain.artist.dto.ArtistWithPostDto;
import ATORY.atory.domain.artist.service.ArtistService;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.user.dto.CustomUserDetails;
import ATORY.atory.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/artist_detail")
@Tag(name = "artist_detail", description = "작가노트 세부사항 API")
public class ArtistDetailController {
    private final ArtistService artistService;


    @Operation(
            summary = "작가노트 조회", description = "작가 노트를 조회합니다.",
            parameters = {
                    @Parameter(name = "id", description = "작가 ID", example = "1")
            }
    )
    @GetMapping("/{id}/artist_note")
    public ArtistWithArtistNoteDto findArtistNoteById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        return artistService.getArtistNoteById(id,user);
    }


    @Operation(
            summary = "작가 게시물 조회", description = "작가 게시물을 조회합니다." ,
            parameters = {
                    @Parameter(name = "id", description = "작가 ID", example = "1"),
                    @Parameter(name = "type", description = "게시물 타입", example = "ART"),
                    @Parameter(name = "tag", description = "검색할 태그명", example = "watercolor"),
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
                    @Parameter(name = "size", description = "한 페이지 게시물 개수", example = "9")
            }
    )
    @GetMapping("/{id}/posts")
    public ArtistWithPostDto findPostsByIdAndTag(@PathVariable Long id,@RequestParam(value = "type") PostType postType,
                                                 @RequestParam(value = "tag",required = false) String tag, Pageable pageable,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        return artistService.getPostByIdAndTag(id,postType,tag,pageable,user);

    }

    @Operation(
            summary = "작가 아카이브 조회", description = "작가 아카이브를 조회합니다." ,
            parameters = {
                    @Parameter(name = "id", description = "작가 ID", example = "1"),
                    @Parameter(name = "type", description = "게시물 타입", example = "ART"),
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
                    @Parameter(name = "size", description = "한 페이지 게시물 개수", example = "9")
            }
    )
    @GetMapping("/{id}/archives")
    public ArtistWithPostDto findArchivesByIdAndType(@PathVariable Long id,
                                                     @RequestParam(value = "type",required = false) PostType postType, Pageable pageable,
                                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

            return artistService.getArchiveByIdAndType(id,postType,pageable,user);

    }
}
