package ATORY.atory.domain.artist.controller;

import ATORY.atory.domain.artist.dto.ArtistWithArtistNoteDto;
import ATORY.atory.domain.artist.dto.ArtistWithPostDto;
import ATORY.atory.domain.artist.service.ArtistService;
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
@RequestMapping("api/artist")
@Tag(name = "artist", description = "작가관련 API")
public class ArtistController {
    private final ArtistService artistService;


    @Operation(
            summary = "작가노트 조회", description = "작가 노트를 조회합니다.",
            parameters = {
            @Parameter(name = "id", description = "작가 ID", example = "1")
            }
    )
    @GetMapping("/{id}")
    public ArtistWithArtistNoteDto findArtistNoteById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return artistService.getArtistNoteById(id,user);
    }


    @Operation(
            summary = "작가 게시물 조회", description = "작가 게시물을 조회합니다.",
            parameters = {
                @Parameter(name = "id", description = "작가 ID", example = "1"),
                @Parameter(name = "type", description = "게시물 타입", example = "ART"),
                @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
                @Parameter(name = "size", description = "한 페이지 게시물 개수", example = "9")
            }
    )
    @GetMapping("/{id}/posts")
    public ArtistWithPostDto findPostsById(@PathVariable Long id, @RequestParam("type") String postType, Pageable pageable,@AuthenticationPrincipal User user) {
        return artistService.getPostById(id,postType,pageable,user);
    }


    @Operation(
            summary = "작가 게시물 태그 조회", description = "작가 게시물을 태그로 조회합니다." ,
            parameters = {
                    @Parameter(name = "id", description = "작가 ID", example = "1"),
                    @Parameter(name = "type", description = "게시물 타입", example = "ART"),
                    @Parameter(name = "tag", description = "검색할 태그명", example = "watercolor"),
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
                    @Parameter(name = "size", description = "한 페이지 게시물 개수", example = "9")
            }
    )
    @GetMapping("/{id}/posts/tag")
    public ArtistWithPostDto findPostsByIdAndTag(@PathVariable Long id,@RequestParam("type") String postType,
                                                 @RequestParam("tag") String tag, Pageable pageable,
                                                 @AuthenticationPrincipal User user) {
        return artistService.getPostByIdAndTag(id,postType,tag,pageable,user);
    }

    @Operation(
            summary = "작가 아카이브 전체 조회", description = "작가 아카리브를 전체 조회합니다." ,
            parameters = {
                    @Parameter(name = "id", description = "작가 ID", example = "1"),
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
                    @Parameter(name = "size", description = "한 페이지 게시물 개수", example = "9")
            }
    )
    @GetMapping("/{id}/archives")
    public ArtistWithPostDto findArchivesById(@PathVariable Long id, Pageable pageable,@AuthenticationPrincipal User user) {
        return artistService.getArchivesById(id,pageable,user);
    }

    @Operation(
            summary = "작가 아카이브 타입별 조회", description = "작가 아카이브를 타입별로 조회합니다." ,
            parameters = {
                    @Parameter(name = "id", description = "작가 ID", example = "1"),
                    @Parameter(name = "type", description = "게시물 타입", example = "ART"),
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
                    @Parameter(name = "size", description = "한 페이지 게시물 개수", example = "9")
            }
    )
    @GetMapping("/{id}/archives/type")
    public ArtistWithPostDto findArchivesByIdAndType(@PathVariable Long id,
                                                     @RequestParam("type") String postType, Pageable pageable,
                                                     @AuthenticationPrincipal User user) {
        return artistService.getArtistByIdAndType(id,postType,pageable,user);
    }
}
