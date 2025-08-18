package ATORY.atory.domain.artist.artistNote.controller;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
import ATORY.atory.domain.artist.artistNote.service.ArtistNoteService;
import ATORY.atory.domain.artist.dto.ArtistDto;
import ATORY.atory.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/artist_note")
@RequiredArgsConstructor
public class ArtistNoteController {
    private final ArtistNoteService artistNoteService;

    @Operation(summary = "작가 정보 리스트 조회", description = "메인 페이지 작가 노트 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/main")
    public ApiResult<Page<ArtistDto>> getMainArtistNotes(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam String googleID){
        Pageable pageable = PageRequest.of(page, size);

        return ApiResult.ok(artistNoteService.loadArtistNotes(pageable, googleID));
    }

    @Operation(summary = "작가 노트 조회", description = "작가 노트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("")
    public ApiResult<List<ArtistNoteDto>> getArtistNotes(@RequestParam String googleID, @RequestParam Long userID){
        return ApiResult.ok(artistNoteService.loadArtistNote(googleID, userID));
    }
}
