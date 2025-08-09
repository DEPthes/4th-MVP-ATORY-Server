package ATORY.atory.domain.artist.controller;

import ATORY.atory.domain.artist.dto.ArtistDto;
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
@RequestMapping("api/artist")
@Tag(name = "artist", description = "작가관련 API")
public class ArtistController {
    private final ArtistService artistService;


    @Operation(
            summary = "작가 정보 조회", description = "작가 정보를 조회합니다.",
            parameters = {
                    @Parameter(name = "id", description = "작가 ID", example = "1")
            }
    )
    @GetMapping("/{id}")
    public ArtistDto findArtistNoteById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        return artistService.getArtistDtoById(id,user);
    }
}
