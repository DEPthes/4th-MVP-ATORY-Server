package ATORY.atory.domain.artist.controller;

import ATORY.atory.domain.artist.dto.ArtistDto;
import ATORY.atory.domain.artist.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/artist")
public class ArtistController {
    private final ArtistService artistService;

    @GetMapping("/{id}")
    public ArtistDto findById(Long id) {
        return artistService.findById(id);
    }

}
