package ATORY.atory.domain.artist.entity;

import ATORY.atory.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "YouthArtist")
public class YouthArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private LocalDate birthDate; // 생년월일
    private String educationBackground; // 학력
    private Boolean isEducationPublic = true; // 학력 공개 여부
    private String statusMessage; // 작가 상태메시지
    private String bannerImageUrl; // 배너 이미지
    private String englishName; // 영어 이름
    private Boolean isContactPublic = Boolean.TRUE; // 연락처 공개 여부
    private Boolean isEmailPublic = Boolean.TRUE; // 이메일 공개 여부

    public YouthArtist(User user, LocalDate birthDate, String educationBackground, Boolean isEducationPublic, String statusMessage, String bannerImageUrl, String englishName, Boolean isContactPublic, Boolean isEmailPublic) {
        this.user = user;
        this.birthDate = birthDate;
        this.educationBackground = educationBackground;
        this.isEducationPublic = isEducationPublic;
        this.statusMessage = statusMessage;
        this.bannerImageUrl = bannerImageUrl;
        this.englishName = englishName;
        this.isContactPublic = isContactPublic;
        this.isEmailPublic = isEmailPublic;
    }

    public void updateProfile(LocalDate birthDate, String educationBackground, Boolean isEducationPublic) {
        this.birthDate = birthDate;
        this.educationBackground = educationBackground;
        this.isEducationPublic = isEducationPublic;
    }

    // 정적 팩토리 메서드
    public static YouthArtist createYouthArtist(User user, LocalDate birthDate, String educationBackground, Boolean isEducationPublic) {
        YouthArtist youthArtist = new YouthArtist();
        youthArtist.user = user;
        youthArtist.birthDate = birthDate;
        youthArtist.educationBackground = educationBackground;
        youthArtist.isEducationPublic = isEducationPublic;
        return youthArtist;
    }
}
