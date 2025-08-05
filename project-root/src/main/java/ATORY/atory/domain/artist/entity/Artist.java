package ATORY.atory.domain.artist.entity;

import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import ATORY.atory.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "Artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY)
    private List<ArtistNote> artistNotes = new ArrayList<>();



    private String birth;
    private String educationBackground;


    private Boolean disclosureStatus = Boolean.TRUE;

    @Builder
    public Artist(User user,List<ArtistNote> artistNotes, String birth, String educationBackground, Boolean disclosureStatus) {
        this.user = user;
        this.artistNotes = artistNotes;
        this.birth = birth;
        this.educationBackground = educationBackground;
        this.disclosureStatus = disclosureStatus;
    }
}
