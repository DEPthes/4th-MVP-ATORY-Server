package ATORY.atory.domain.artist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArtistRegisterDto {

    @Schema(description = "작가 이름")
    private String name;

    @Schema(description = "구글 sub")
    private String googleID;

    @Schema(description = "작가 이메일")
    private String email;

    @Schema(description = "작가 소개글")
    private String introduction;

    @Schema(description = "작가 연락처")
    private String contact;

    @Schema(description = "작가 생일")
    private String birth;

    @Schema(description = "작가 학력")
    private String educationBackground;

    @Schema(description = "학력 공개 여부")
    private Boolean disclosureStatus;
}
