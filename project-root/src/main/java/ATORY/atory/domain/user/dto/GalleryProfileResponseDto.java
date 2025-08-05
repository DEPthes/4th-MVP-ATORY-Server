package ATORY.atory.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GalleryProfileResponseDto {
    private Long userId;
    private String galleryName;
    private String message;
} 