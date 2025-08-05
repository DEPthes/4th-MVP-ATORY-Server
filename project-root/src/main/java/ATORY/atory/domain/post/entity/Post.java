package ATORY.atory.domain.post.entity;

import ATORY.atory.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, optional = false)
    private PostDate postDate;


    @Builder
    public Post(Long id, User user, String name, String imageURL, String exhibitionURL, String description, PostType postType,PostDate postDate) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.imageURL = imageURL;
        this.exhibitionURL = exhibitionURL;
        this.description = description;
        this.postType = postType;
        this.postDate = postDate;
    }
}
