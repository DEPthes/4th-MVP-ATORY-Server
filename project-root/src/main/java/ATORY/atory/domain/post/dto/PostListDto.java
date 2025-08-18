package ATORY.atory.domain.post.dto;

import ATORY.atory.domain.post.entity.PostType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostListDto {
    private String postId;
    private PostType postType;
    private String title;
    private List<String> imageUrls;
    private String userName;
    private Long archived;
    private Boolean isMine;
    private Boolean isArchived;
}
