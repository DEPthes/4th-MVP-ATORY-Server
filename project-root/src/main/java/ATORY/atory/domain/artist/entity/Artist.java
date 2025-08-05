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
    private String statusMessage; // 작가 상태메시지
    private String bannerImageUrl; // 배너 이미지
    private String englishName; // 영어 이름
    private Boolean isContactPublic = Boolean.TRUE; // 연락처 공개 여부
    private Boolean isEmailPublic = Boolean.TRUE; // 이메일 공개 여부

    private Boolean disclosureStatus = Boolean.TRUE;

    @Builder
    public Artist(User user, String birth, String educationBackground, Boolean disclosureStatus, String statusMessage, String bannerImageUrl, String englishName, Boolean isContactPublic, Boolean isEmailPublic) {
        this.user = user;
        this.birth = birth;
        this.educationBackground = educationBackground;
        this.disclosureStatus = disclosureStatus;
        this.statusMessage = statusMessage;
        this.bannerImageUrl = bannerImageUrl;
        this.englishName = englishName;
        this.isContactPublic = isContactPublic;
        this.isEmailPublic = isEmailPublic;
    }
}
