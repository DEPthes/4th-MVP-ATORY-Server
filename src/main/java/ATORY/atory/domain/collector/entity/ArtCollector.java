package ATORY.atory.domain.collector.entity;

import ATORY.atory.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "ArtCollector")
public class ArtCollector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private LocalDate birthDate; // 생년월일
    private String educationBackground; // 학력
    private Boolean isEducationPublic = true; // 학력 공개 여부

    public ArtCollector(Long id, User user, LocalDate birthDate, String educationBackground, Boolean isEducationPublic) {
        this.id = id;
        this.user = user;
        this.birthDate = birthDate;
        this.educationBackground = educationBackground;
        this.isEducationPublic = isEducationPublic;
    }

    public void updateProfile(LocalDate birthDate, String educationBackground, Boolean isEducationPublic) {
        this.birthDate = birthDate;
        this.educationBackground = educationBackground;
        this.isEducationPublic = isEducationPublic;
    }

    // 정적 팩토리 메서드
    public static ArtCollector createArtCollector(User user, LocalDate birthDate, String educationBackground, Boolean isEducationPublic) {
        ArtCollector artCollector = new ArtCollector();
        artCollector.user = user;
        artCollector.birthDate = birthDate;
        artCollector.educationBackground = educationBackground;
        artCollector.isEducationPublic = isEducationPublic;
        return artCollector;
    }
}
