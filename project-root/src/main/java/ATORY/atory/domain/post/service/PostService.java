package ATORY.atory.domain.post.service;

import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;

    public Page<PostDto> findPostsByUserIdAndPostType(Long userId, PostType postType, Pageable pageable) {
        Page<Post> posts = postRepository.findPostsByUserIdAndPostType(userId, postType, pageable);
        return posts.map(this::toDto);
    }

    public Page<PostDto> findPostsByUserIdAndPostType(Long userId, String postType, Pageable pageable) {
        PostType type = PostType.valueOf(postType);
        return findPostsByUserIdAndPostType(userId, type, pageable);
    }

    private PostDto toDto(Post p) {
        return PostDto.builder()
                .id(p.getId())
                .user(p.getUser())
                .name(p.getName())
                .imageURL(p.getImageURL())
                .exhibitionURL(p.getExhibitionURL())
                .description(p.getDescription())
                .postType(p.getPostType())
                .build();
    }
}