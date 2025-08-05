package ATORY.atory.domain.post.dto;

import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

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
    private PostDateDto postDate;
    private Long archived;

    @Builder
    public PostDto(Long id, User user, String name, String imageURL, String exhibitionURL, String description, PostType postType,PostDateDto postDate,Long archived) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.imageURL = imageURL;
        this.exhibitionURL = exhibitionURL;
        this.description = description;
        this.postType = postType;
        this.postDate = postDate;
        this.archived = archived;
    }
}
