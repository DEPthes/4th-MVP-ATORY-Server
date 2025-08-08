package ATORY.atory.domain.post.dto;

import ATORY.atory.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PostDateDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Post post;

    @Builder
    public PostDateDto(Long id, Post post, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.post = post;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
