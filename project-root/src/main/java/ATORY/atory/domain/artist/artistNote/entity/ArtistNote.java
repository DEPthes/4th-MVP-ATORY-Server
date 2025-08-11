package ATORY.atory.domain.artist.artistNote.entity;

import ATORY.atory.domain.artist.entity.Artist;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ArtistNote")
public class ArtistNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Enumerated(EnumType.STRING)
    private ArtistNoteType artistNoteType;

    private String year;
    private String description;

    @Builder
    public ArtistNote(Long id, Artist artist, ArtistNoteType artistNoteType, String year, String description) {
        this.id = id;
        this.artist = artist;
        this.artistNoteType = artistNoteType;
        this.year = year;
        this.description = description;
    }
}
