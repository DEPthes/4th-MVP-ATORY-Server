package ATORY.atory.domain.artist.artistNote.controller;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
import ATORY.atory.domain.artist.artistNote.service.ArtistNoteService;
import ATORY.atory.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/artist_note")
@RequiredArgsConstructor
public class ArtistNoteController {


    private final ArtistNoteService artistNoteService;

    @PostMapping
    public ResponseEntity<ArtistNoteDto> create(@RequestBody ArtistNoteDto dto,@AuthenticationPrincipal CustomUserDetails loginUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(artistNoteService.create(dto,loginUser));
    }

    @PatchMapping
    public ResponseEntity<List<ArtistNoteDto>> bulkUpdate(@RequestBody List<ArtistNoteDto> notes,@AuthenticationPrincipal CustomUserDetails loginUser) {
        return ResponseEntity.ok(artistNoteService.updateMultiple(notes,loginUser));
    }


}
