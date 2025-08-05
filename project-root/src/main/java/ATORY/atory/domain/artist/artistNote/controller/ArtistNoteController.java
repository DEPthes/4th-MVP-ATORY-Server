package ATORY.atory.domain.artist.artistNote.controller;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteListResponseDto;
import ATORY.atory.domain.artist.artistNote.service.ArtistNoteService;
import ATORY.atory.global.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/artist-note")
@Tag(name = "Artist Note", description = "작가노트 관련 API")
public class ArtistNoteController {

    private final ArtistNoteService artistNoteService;
    private final JwtProvider jwtProvider;

    @GetMapping
    @Operation(summary = "작가노트 작가 리스트 조회", description = "작가노트에 등록된 작가들의 정보를 페이징하여 조회합니다.")
    public ResponseEntity<Page<ArtistNoteListResponseDto>> getArtistNoteList(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지당 작가 수", example = "10")
            @RequestParam(defaultValue = "10") int size,
            
            HttpServletRequest request) {
        
        // JWT 토큰에서 사용자 ID 추출 (선택적)
        Long currentUserId = null;
        String token = extractTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)) {
            currentUserId = Long.parseLong(jwtProvider.getUserIdFromToken(token));
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ArtistNoteListResponseDto> response = artistNoteService.getArtistNoteList(pageable, currentUserId);
        
        return ResponseEntity.ok(response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
