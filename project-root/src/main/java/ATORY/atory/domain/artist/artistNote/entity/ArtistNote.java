package ATORY.atory.domain.artist.artistNote.entity;

import ATORY.atory.domain.artist.entity.Artist;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Enumerated(EnumType.STRING)
    @Column(name = "artist_note_type", length = 30, nullable = false)
    private ArtistNoteType artistNoteType;

    @Column(length = 10)
    private String year;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Builder
    public ArtistNote(Artist artist,
                      ArtistNoteType artistNoteType,
                      String year,
                      String description,
                      String coverImageUrl) {
        this.artistNoteType = artistNoteType;
        this.year = year;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        if (artist != null) {
            changeArtist(artist);
        }
    }

    public void changeArtist(Artist artist) {
        if (this.artist == artist) return;
        if (this.artist != null && this.artist.getNotes() != null) {
            this.artist.getNotes().remove(this);
        }
        this.artist = artist;
        if (artist != null && artist.getNotes() != null && !artist.getNotes().contains(this)) {
            artist.getNotes().add(this);
        }
    }

    public static ArtistNote create(Artist artist,
                                    ArtistNoteType type,
                                    String year,
                                    String description,
                                    String coverImageUrl) {
        return ArtistNote.builder()
                .artist(artist)
                .artistNoteType(type)
                .year(year)
                .description(description)
                .coverImageUrl(coverImageUrl)
                .build();
    }

    public void changeCoverImageUrl(String url) { this.coverImageUrl = url; }
    public void changeDescription(String description) { this.description = description; }
    public void changeType(ArtistNoteType type) { this.artistNoteType = type; }
    public void changeYear(String year) { this.year = year; }
}