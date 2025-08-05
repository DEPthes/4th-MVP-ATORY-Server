package ATORY.atory.domain.artist.controller;

import ATORY.atory.domain.artist.dto.ArtistWithArtistNoteDto;
import ATORY.atory.domain.artist.dto.ArtistWithPostDto;
import ATORY.atory.domain.artist.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/artist")
public class ArtistController {
    private final ArtistService artistService;

    @GetMapping("/{id}")
    public ArtistWithArtistNoteDto findArtistNoteById(@PathVariable Long id) {
        return artistService.findArtistNoteById(id);
    }

    @GetMapping("/{id}/posts")
    public ArtistWithPostDto findPostsById(@PathVariable Long id, @RequestParam("type") String postType, Pageable pageable) {
        return artistService.findPostById(id,postType,pageable);
    }

    @GetMapping("/{id}/posts/tag")
    public ArtistWithPostDto findPostsByIdAndTag(@PathVariable Long id,@RequestParam("type") String postType, @RequestParam("tag") String tag, Pageable pageable) {
        return artistService.findPostByIdAndTag(id,postType,tag,pageable);
    }
}
