package ATORY.atory.domain.artist.artistNote.entity;

import ATORY.atory.domain.artist.entity.YouthArtist;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "ArtistNote")
public class ArtistNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private YouthArtist youthArtist;

    private ArtistNoteType artistNoteType;

    private String year;
    private String description;

    @Builder
    public ArtistNote(Long id, YouthArtist youthArtist, ArtistNoteType artistNoteType, String year, String description) {
        this.id = id;
        this.youthArtist = youthArtist;
        this.artistNoteType = artistNoteType;
        this.year = year;
        this.description = description;
    }
}
