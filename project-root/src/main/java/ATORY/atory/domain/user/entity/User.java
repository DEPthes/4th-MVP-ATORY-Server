package ATORY.atory.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String phone;
    private String birthDate;
    private Boolean isProfileCompleted = false;

    @Builder
    public User(String username, String googleID, String email, String introduction, String contact, Boolean isSocial, String provider, String profileImgUrl, String phone, String birthDate, Boolean isProfileCompleted) {
        this.username = username;
        this.googleID = googleID;
        this.email = email;
        this.introduction = introduction;
        this.contact = contact;
        this.isSocial = isSocial;
        this.provider = provider;
        this.profileImgUrl = profileImgUrl;
        this.phone = phone;
        this.birthDate = birthDate;
        this.isProfileCompleted = isProfileCompleted;
    }

    public void updateSocialInfo(String googleID, String provider) {
        this.googleID = googleID;
        this.provider = provider;
        this.isSocial = true;
    }

    public void updateProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void updateProfileInfo(String name, String phone, String email, String birthDate, String introduction) {
        this.username = name;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.introduction = introduction;
    }

    public void completeProfile() {
        this.isProfileCompleted = true;
    }
}