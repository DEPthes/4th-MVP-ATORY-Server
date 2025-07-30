package ATORY.atory.domain.artist.entity;

import ATORY.atory.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String birth;
    private String educationBackground;

    private Boolean disclosureStatus = Boolean.TRUE;

    @Builder
    public Artist(User user, String birth, String educationBackground, Boolean disclosureStatus) {
        this.user = user;
        this.birth = birth;
        this.educationBackground = educationBackground;
        this.disclosureStatus = disclosureStatus;
    }
}
