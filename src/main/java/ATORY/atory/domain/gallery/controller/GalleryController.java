package ATORY.atory.domain.gallery.controller;

import ATORY.atory.domain.gallery.dto.GalleryProfileUpdateRequestDto;
import ATORY.atory.domain.gallery.entity.Gallery;
import ATORY.atory.domain.gallery.service.GalleryService;
import ATORY.atory.global.dto.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    @PutMapping("/profile")
    public ResponseEntity<ApiResult<Gallery>> updateProfile(
            @RequestParam Long userId,
            @Valid @RequestBody GalleryProfileUpdateRequestDto requestDto) {
        
        Gallery updatedGallery = galleryService.updateProfile(userId, requestDto);
        return ResponseEntity.ok(ApiResult.success(updatedGallery));
    }
}
