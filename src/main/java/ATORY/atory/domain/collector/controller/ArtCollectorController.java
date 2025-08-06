package ATORY.atory.domain.collector.controller;

import ATORY.atory.domain.collector.dto.ArtCollectorProfileUpdateRequestDto;
import ATORY.atory.domain.collector.entity.ArtCollector;
import ATORY.atory.domain.collector.service.ArtCollectorService;
import ATORY.atory.global.dto.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/art-collector")
@RequiredArgsConstructor
public class ArtCollectorController {

    private final ArtCollectorService artCollectorService;

    @PutMapping("/profile")
    public ResponseEntity<ApiResult<ArtCollector>> updateProfile(
            @RequestParam Long userId,
            @Valid @RequestBody ArtCollectorProfileUpdateRequestDto requestDto) {
        
        ArtCollector updatedArtCollector = artCollectorService.updateProfile(userId, requestDto);
        return ResponseEntity.ok(ApiResult.success(updatedArtCollector));
    }
} 