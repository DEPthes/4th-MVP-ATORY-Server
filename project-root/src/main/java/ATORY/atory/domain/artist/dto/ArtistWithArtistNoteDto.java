package ATORY.atory.domain.artist.dto;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
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
@Schema(description = "작가 정보 및 작가노트 DTO")
public class ArtistWithArtistNoteDto {

    @Schema(description = "작가 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이름", example = "김철수")
    private String username;

    @Schema(description = "작가가 작성한 작가노트 목록")
    private List<ArtistNoteDto> artistNotes;

    @Schema(description = "요청자가 해당 작가 본인인지 여부", example = "false")
    private Boolean owner;

    @Schema(description = "요청자 로그인 여부", example = "false")
    private Boolean login;


    @Builder
    public ArtistWithArtistNoteDto(Long id, String username,List<ArtistNoteDto> artistNotes,
                                   Boolean owner, Boolean login) {
        this.id = id;
        this.username = username;
        this.artistNotes = artistNotes;
        this.owner = owner;
        this.login = login;
    }
}
