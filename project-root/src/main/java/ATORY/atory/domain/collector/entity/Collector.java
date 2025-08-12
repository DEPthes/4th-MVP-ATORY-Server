package ATORY.atory.domain.collector.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ATORY.atory.domain.user.entity.User;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "Collector")
public class Collector {

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
    public Collector(Long id, User user, String birth, String educationBackground, Boolean disclosureStatus) {
        this.id = id;
        this.user = user;
        this.birth = birth;
        this.educationBackground = educationBackground;
        this.disclosureStatus = disclosureStatus;
    }
}
