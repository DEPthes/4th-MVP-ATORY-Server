package ATORY.atory.domain.artist.controller;

import ATORY.atory.domain.artist.dto.YouthArtistProfileUpdateRequestDto;
import ATORY.atory.domain.artist.entity.YouthArtist;
import ATORY.atory.domain.artist.service.YouthArtistService;
import ATORY.atory.global.dto.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/youth-artist")
@RequiredArgsConstructor
public class YouthArtistController {

    private final YouthArtistService youthArtistService;

    @PutMapping("/profile")
    public ResponseEntity<ApiResult<YouthArtist>> updateProfile(
            @RequestParam Long userId,
            @Valid @RequestBody YouthArtistProfileUpdateRequestDto requestDto) {
        
        YouthArtist updatedYouthArtist = youthArtistService.updateProfile(userId, requestDto);
        return ResponseEntity.ok(ApiResult.success(updatedYouthArtist));
    }
} 