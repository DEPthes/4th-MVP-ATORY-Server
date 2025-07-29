package ATORY.atory.domain.archive.dto;

import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArchiveDto {

    private Long id;
    private User user;
    private Post post;

    @Builder
    public ArchiveDto(Long id, User user, Post post) {
        this.id = id;
        this.user = user;
        this.post = post;
    }
}
