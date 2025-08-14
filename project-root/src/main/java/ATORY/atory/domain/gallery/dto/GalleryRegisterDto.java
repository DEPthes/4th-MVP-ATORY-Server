package ATORY.atory.domain.gallery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GalleryRegisterDto {
    @Schema(description = "유저 이름")
    private String userName;

    @Schema(description = "구글 sub")
    private String googleID;

    @Schema(description = "유저 이메일")
    private String email;

    @Schema(description = "유저 소개글")
    private String introduction;

    @Schema(description = "유저 연락처")
    private String contact;

    @Schema(description = "갤러리 이름")
    private String galleryName;

    @Schema(description = "갤러리 위치")
    private String location;

    @Schema(description = "사업자등록번호")
    private String registrationNumber;
}
