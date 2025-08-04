package ATORY.atory.domain.user.entity;

import ATORY.atory.domain.follow.entity.Follow;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    // 내가 팔로우 하는 사람들
    @OneToMany(mappedBy = "follower")
    private List<Follow> followings = new ArrayList<>();

    // 나를 팔로우 하는 사람들
    @OneToMany(mappedBy = "following")
    private List<Follow> followers = new ArrayList<>();

    @Column(columnDefinition = "JSON", nullable = true)
    private String profileImageURL;

    @Column(columnDefinition = "JSON", nullable = true)
    private String coverImageURL;


    @Builder
    public User(String username, String googleID, String email, String introduction, String contact, List<Follow> followings, List<Follow> followers, String profileImageURL, String coverImageURL) {
        this.username = username;
        this.googleID = googleID;
        this.email = email;
        this.introduction = introduction;
        this.contact = contact;
        this.followings = followings;
        this.followers = followers;
        this.profileImageURL = profileImageURL;
        this.coverImageURL = coverImageURL;
    }
}
