package ATORY.atory.domain.tag.dto;

import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.tag.entity.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TagPostDto {

    private Long id;
    private Post post;
    private Tag tag;

    @Builder
    public TagPostDto(Long id, Post post, Tag tag) {
        this.id = id;
        this.post = post;
        this.tag = tag;
    }
}
