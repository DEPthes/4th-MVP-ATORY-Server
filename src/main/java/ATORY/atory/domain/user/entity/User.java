package ATORY.atory.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String googleID;
    private String email;
    private String introduction;
    private String contact;
    private Boolean isSocial;
    private String provider;
    private String profileImgUrl;
    private String coverImgUrl; // 커버 사진 추가
    private String phone;
    private String birthDate;
    private Boolean isProfileCompleted = false;



    public void updateSocialInfo(String googleID, String provider) {
        this.googleID = googleID;
        this.provider = provider;
        this.isSocial = true;
    }

    public void updateProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void updateCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public void updateProfileInfo(String username, String phone, String email, String introduction, String profileImgUrl, String coverImgUrl) {
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.introduction = introduction;
        if (profileImgUrl != null) {
            this.profileImgUrl = profileImgUrl;
        }
        if (coverImgUrl != null) {
            this.coverImgUrl = coverImgUrl;
        }
    }

    public void completeProfile() {
        this.isProfileCompleted = true;
    }

    // 정적 팩토리 메서드들
    public static User createUser(String username, String email, String googleID, String provider) {
        User user = new User();
        user.username = username;
        user.email = email;
        user.googleID = googleID;
        user.provider = provider;
        user.isSocial = true;
        user.isProfileCompleted = false;
        return user;
    }

    public static User createUserWithProfile(String username, String email, String googleID, String provider, 
                                          String phone, String introduction, String profileImgUrl, String coverImgUrl) {
        User user = createUser(username, email, googleID, provider);
        user.phone = phone;
        user.introduction = introduction;
        user.profileImgUrl = profileImgUrl;
        user.coverImgUrl = coverImgUrl;
        return user;
    }
}