package ATORY.atory.domain.artist.artistNote.dto;

import ATORY.atory.domain.artist.artistNote.entity.ArtistNoteType;
import ATORY.atory.domain.artist.entity.Artist;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArtistNoteDto {
    private Long id;
    private Artist artist;
    private ArtistNoteType artistNoteType;
    private String year;
    private String description;

    @Builder
    public ArtistNoteDto(Long id, Artist artist, ArtistNoteType artistNoteType, String year, String description) {
        this.id = id;
        this.artist = artist;
        this.artistNoteType = artistNoteType;
        this.year = year;
        this.description = description;
    }
}
