package ATORY.atory.domain.artist.artistNote.controller;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteListResponseDto;
import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDetailResponseDto;
import ATORY.atory.domain.artist.artistNote.service.ArtistNoteService;
import ATORY.atory.global.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/artist-notes")
@Tag(name = "Artist Note", description = "작가노트 관련 API")
public class ArtistNoteController {

    private final ArtistNoteService artistNoteService;
    private final JwtProvider jwtProvider;

    @GetMapping
    @Operation(summary = "작가노트 목록 조회", description = "작가노트 목록을 페이징하여 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Page<ArtistNoteListResponseDto>> getArtistNoteList(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지당 항목 수", example = "10")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "정렬 기준", example = "createdAt,desc")
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            
            HttpServletRequest request) {
        
        // JWT 토큰에서 사용자 ID 추출 (선택적)
        Long currentUserId = null;
        String token = extractTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)) {
            currentUserId = Long.parseLong(jwtProvider.getUserIdFromToken(token));
        }
        
        // 정렬 설정
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && "desc".equals(sortParams[1]) 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<ArtistNoteListResponseDto> response = artistNoteService.getArtistNoteList(pageable, currentUserId);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{noteId}")
    @Operation(summary = "작가노트 상세 조회", description = "특정 작가노트의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "작가노트를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ArtistNoteDetailResponseDto> getArtistNoteDetail(
            @Parameter(description = "작가노트 ID", example = "1")
            @PathVariable Long noteId,
            
            HttpServletRequest request) {
        
        // JWT 토큰에서 사용자 ID 추출 (선택적)
        Long currentUserId = null;
        String token = extractTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)) {
            currentUserId = Long.parseLong(jwtProvider.getUserIdFromToken(token));
        }
        
        ArtistNoteDetailResponseDto response = artistNoteService.getArtistNoteDetail(noteId, currentUserId);
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
