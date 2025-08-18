package ATORY.atory.domain.user.entity;

import ATORY.atory.global.dto.UserType;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    private Long id;

    private String username;
    private String googleID;
    private String email;
    private String introduction;
    private String contact;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20)")
    private UserType userType;

    @Column(columnDefinition = "JSON", nullable = true)
    private String profileImageURL;

    @Column(columnDefinition = "JSON", nullable = true)
    private String coverImageURL;


    @Builder
    public User(String username, String googleID, String email, String introduction, String contact,  String profileImageURL, String coverImageURL, UserType userType) {
        this.username = username;
        this.googleID = googleID;
        this.email = email;
        this.introduction = introduction;
        this.contact = contact;
        this.profileImageURL = profileImageURL;
        this.coverImageURL = coverImageURL;
        this.userType = userType;
    }

    public void changeProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public void changeCoverImageURL(String coverImageURL) {
        this.coverImageURL = coverImageURL;
    }
}
