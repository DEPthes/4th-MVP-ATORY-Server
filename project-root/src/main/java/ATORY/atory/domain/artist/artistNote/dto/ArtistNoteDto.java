package ATORY.atory.domain.artist.artistNote.dto;

import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import ATORY.atory.domain.artist.artistNote.entity.ArtistNoteType;
import ATORY.atory.domain.artist.entity.Artist;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@Schema(description = "작가 노트 DTO")
public class ArtistNoteDto {
    @Schema(description = "작가 노트 ID", example = "101")
    private Long id;
    @Schema(description = "작가 노트 유형", example = "HISTORY")
    private ArtistNoteType artistNoteType;

    @Schema(description = "연도", example = "2023")
    private String year;

    @Schema(description = "노트 내용", example = "서울 국제 아트페어 참가")
    private String description;


    @Builder
    public ArtistNoteDto(Long id, ArtistNoteType artistNoteType, String year, String description) {
        this.id = id;
        this.artistNoteType = artistNoteType;
        this.year = year;
        this.description = description;
    }

    public static ArtistNoteDto from(ArtistNote artistNote) {
        return ArtistNoteDto.builder()
                .id(artistNote.getId())
                .artistNoteType(artistNote.getArtistNoteType())
                .year(artistNote.getYear())
                .description(artistNote.getDescription())
                .build();
    }

    public static List<ArtistNoteDto> from(List<ArtistNote> artistNotes) {
        return artistNotes.stream()
                .map(ArtistNoteDto::from)
                .collect(Collectors.toList());
    }
}
