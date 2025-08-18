package ATORY.atory.domain.artist.artistNote.dto;

import ATORY.atory.domain.artist.artistNote.entity.ArtistNoteType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArtistNoteSaveDto {
    @Schema(description = "작가 노트 유형", example = "HISTORY")
    private ArtistNoteType artistNoteType;

    @Schema(description = "연도", example = "2023")
    private String year;

    @Schema(description = "노트 내용", example = "서울 국제 아트페어 참가")
    private String description;

    @Builder
    public ArtistNoteSaveDto(ArtistNoteType artistNoteType, String year, String description) {
        this.artistNoteType = artistNoteType;
        this.year = year;
        this.description = description;
    }
}
