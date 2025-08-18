package ATORY.atory.domain.post.entity;

import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.dto.PostSaveDto;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    @SequenceGenerator(
            name = "post_seq",
            sequenceName = "post_sequence",
            allocationSize = 1
    )
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

    public void updatePost(PostSaveDto dto, String imageURL, String exhibitionURL) {
        this.name = dto.getTitle();
        this.imageURL = imageURL;
        this.exhibitionURL = exhibitionURL;
        this.description = dto.getDescription();
        this.postType = dto.getPostType();
    }
}
