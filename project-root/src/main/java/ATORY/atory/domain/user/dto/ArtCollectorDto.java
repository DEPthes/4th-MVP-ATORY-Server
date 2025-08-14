package ATORY.atory.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Schema(description = "아트컬랙터 기본 정보 DTO")
public class ArtCollectorDto extends UserDto {
    @Schema(description = "아트컬랙터 생년월일", example = "1980-01-01")
    private String birth;

    @Schema(description = "학력 정보", example = "명지대학교 미술학과 졸업")
    private String educationBackground;

    @Schema(description = "정보 공개 여부", example = "true")
    private Boolean disclosureStatus;
}
