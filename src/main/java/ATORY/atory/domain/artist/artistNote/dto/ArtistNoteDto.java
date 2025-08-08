package ATORY.atory.domain.artist.artistNote.dto;

import ATORY.atory.domain.artist.artistNote.entity.ArtistNoteType;
import ATORY.atory.domain.artist.entity.YouthArtist;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArtistNoteDto {
    private Long id;
    private YouthArtist youthArtist;
    private ArtistNoteType artistNoteType;
    private String year;
    private String description;

    @Builder
    public ArtistNoteDto(Long id, YouthArtist youthArtist, ArtistNoteType artistNoteType, String year, String description) {
        this.id = id;
        this.youthArtist = youthArtist;
        this.artistNoteType = artistNoteType;
        this.year = year;
        this.description = description;
    }
}
