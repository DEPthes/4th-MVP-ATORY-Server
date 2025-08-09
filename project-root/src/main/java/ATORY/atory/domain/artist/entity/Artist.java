package ATORY.atory.domain.artist.entity;

import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import ATORY.atory.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "artist",
        indexes = {
                @Index(name = "ix_artist_user", columnList = "user_id", unique = true)
        }
)
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "birth", length = 10)
    private String birth;

    @Column(name = "education_background", length = 255)
    private String educationBackground;

    @Column(name = "disclosure_status", nullable = false)
    private Boolean disclosureStatus = Boolean.TRUE;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArtistNote> notes = new ArrayList<>();

    @Builder
    public Artist(User user, String birth, String educationBackground, Boolean disclosureStatus) {
        this.user = user;
        this.birth = birth;
        this.educationBackground = educationBackground;
        if (disclosureStatus != null) {
            this.disclosureStatus = disclosureStatus;
        }
    }

    public void addNote(ArtistNote note) {
        if (note == null) return;
        if (!this.notes.contains(note)) {
            this.notes.add(note);
        }
        note.changeArtist(this);
    }

    public void removeNote(ArtistNote note) {
        if (note == null) return;
        if (this.notes.remove(note)) {
            note.changeArtist(null);
        }
    }
}