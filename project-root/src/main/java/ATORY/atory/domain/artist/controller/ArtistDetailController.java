package ATORY.atory.domain.artist.controller;

import ATORY.atory.domain.artist.dto.ArtistWithArtistNoteDto;
import ATORY.atory.domain.user.dto.UserWithPostDto;
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
}
