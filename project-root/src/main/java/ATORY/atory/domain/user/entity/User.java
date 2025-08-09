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
    private String englishName;
    private String googleID;
    private String email;
    private String introduction;
    private String contact;
    private String profileImgUrl;


    private String profileImageUrl;
    @Builder
    public User(String username,
                String englishName,
                String googleID,
                String email,
                String introduction,
                String contact,
                String profileImgUrl) {
        this.username = username;
        this.englishName = englishName;
        this.googleID = googleID;
        this.email = email;
        this.introduction = introduction;
        this.contact = contact;
        this.profileImgUrl = profileImgUrl;
    }
}