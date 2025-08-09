package ATORY.atory.domain.artist.dto;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Schema(description = "작가 정보 DTO")
public class ArtistDto {

    @Schema(description = "작가 ID", example = "1")
    private Long id;

    @Schema(description = "작가의 사용자 정보")
    private UserDto user;

    @Schema(description = "작가 생년월일", example = "1980-01-01")
    private String birth;

    @Schema(description = "학력 정보", example = "명지대학교 미술학과 졸업")
    private String educationBackground;

    @Schema(description = "정보 공개 여부", example = "true")
    private Boolean disclosureStatus;

    @Schema(description = "요청자가 해당 작가 본인인지 여부", example = "false")
    private Boolean owner;

    @Schema(description = "요청자 로그인 여부", example = "false")
    private Boolean login;


    @Builder
    public ArtistDto(Long id, UserDto user,
                                   String birth, String educationBackground,
                                   Boolean disclosureStatus, Boolean owner, Boolean login) {
        this.id = id;
        this.user = user;
        this.birth = birth;
        this.educationBackground = educationBackground;
        this.disclosureStatus = disclosureStatus;
        this.owner = owner;
        this.login = login;
    }
}
