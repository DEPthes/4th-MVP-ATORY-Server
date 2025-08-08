package ATORY.atory.domain.post.dto;

import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostDto {

    private Long id;
    private User user;
    private String name;
    private String imageURL;
    private String exhibitionURL;
    private String description;
    private PostType postType;

    @Builder
    public PostDto(Long id, User user, String name, String imageURL, String exhibitionURL, String description, PostType postType) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.imageURL = imageURL;
        this.exhibitionURL = exhibitionURL;
        this.description = description;
        this.postType = postType;
    }
}
