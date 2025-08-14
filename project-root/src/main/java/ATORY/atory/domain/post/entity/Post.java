package ATORY.atory.domain.post.entity;

import ATORY.atory.domain.tag.entity.TagPost;
import ATORY.atory.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    @Column(columnDefinition = "JSON", nullable = true)
    private String imageURL;

    @Column(columnDefinition = "JSON", nullable = true)
    private String exhibitionURL;

    private String description;
    private PostType postType;


    @Builder
    public Post(Long id, User user, String name, String imageURL, String exhibitionURL, String description, PostType postType) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.imageURL = imageURL;
        this.exhibitionURL = exhibitionURL;
        this.description = description;
        this.postType = postType;
    }
}
