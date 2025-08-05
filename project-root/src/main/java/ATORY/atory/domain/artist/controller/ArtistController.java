package ATORY.atory.domain.artist.controller;

import ATORY.atory.domain.artist.dto.ArtistProfileResponseDto;
import ATORY.atory.domain.artist.dto.ArtistNoteExistsResponseDto;
import ATORY.atory.domain.artist.service.ArtistService;
import ATORY.atory.global.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/author")
@Tag(name = "Artist", description = "작가 관련 API")
public class ArtistController {

    private final ArtistService artistService;
    private final JwtProvider jwtProvider;

    @GetMapping("/profile/{authorId}")
    @Operation(summary = "작가 프로필 조회", description = "작가의 프로필 정보를 조회합니다.")
    public ResponseEntity<ArtistProfileResponseDto> getArtistProfile(
            @PathVariable Long authorId,
            HttpServletRequest request) {
        
        // JWT 토큰에서 사용자 ID 추출 (선택적)
        Long currentUserId = null;
        String token = extractTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)) {
            currentUserId = Long.parseLong(jwtProvider.getUserIdFromToken(token));
        }
        
        ArtistProfileResponseDto response = artistService.getArtistProfile(authorId, currentUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/note/exists/{authorId}")
    @Operation(summary = "작가노트 존재 여부 확인", description = "작가의 작가노트 게시물 존재 여부를 확인합니다.")
    public ResponseEntity<ArtistNoteExistsResponseDto> checkArtistNoteExists(
            @PathVariable Long authorId) {
        
        ArtistNoteExistsResponseDto response = artistService.checkArtistNoteExists(authorId);
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
