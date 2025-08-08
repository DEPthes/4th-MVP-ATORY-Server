package ATORY.atory.domain.gallery.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GalleryProfileUpdateRequestDto {

    @NotBlank(message = "갤러리명은 필수입니다.")
    private String name;

    @NotBlank(message = "사업자 등록번호는 필수입니다.")
    private String registrationNumber;

    @NotBlank(message = "위치 정보는 필수입니다.")
    private String location;
} 