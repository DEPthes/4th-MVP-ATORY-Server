package ATORY.atory.domain.artist.artistNote.controller;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDetailResponseDto;
import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteListItemDto;
import ATORY.atory.domain.artist.artistNote.service.ArtistNoteService;
import ATORY.atory.global.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/artist_note")
@RequiredArgsConstructor
public class ArtistNoteController {

    private final ArtistNoteService artistNoteService;

    // 목록 조회
    @GetMapping
    public ApiResult<Page<ArtistNoteListItemDto>> getNotes(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ArtistNoteListItemDto> page = artistNoteService.getNotes(pageable);
        return ApiResult.success(page);
    }

    // 상세 조회
    @GetMapping("/{noteId}")
    public ApiResult<ArtistNoteDetailResponseDto> getNote(@PathVariable Long noteId) {
        return ApiResult.success(artistNoteService.getNote(noteId));
    }
}