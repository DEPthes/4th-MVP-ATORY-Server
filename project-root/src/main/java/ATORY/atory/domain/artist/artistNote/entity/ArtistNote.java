package ATORY.atory.domain.artist.artistNote.entity;

import ATORY.atory.domain.artist.entity.Artist;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "artist_note")
public class ArtistNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Enumerated(EnumType.STRING)
    @Column(name = "artist_note_type", nullable = false, length = 20)
    private ArtistNoteType artistNoteType;

    @Column(name = "note_year")
    private String year;

    @Column(columnDefinition = "TEXT")
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