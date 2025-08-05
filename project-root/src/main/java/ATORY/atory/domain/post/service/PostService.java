package ATORY.atory.domain.post.service;

import ATORY.atory.domain.archive.service.ArchiveService;
import ATORY.atory.domain.post.dto.PostDateDto;
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.repository.PostRepository;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
    private final PostRepository postRepository;
    private final ArchiveService archiveService;
    public Page<PostDto> findPostsByUserIdAndPostType(Long id, String postType,Pageable pageable) {
        Page<Post> posts = postRepository.findPostsByUserIdAndPostType(id,postType,pageable);
        if (posts.isEmpty()) {
            throw new MapperException(ErrorCode.SER_NOT_FOUND);
        }

        return posts.map(post -> {
            Long archiveCount = archiveService.getArchiveCountByPostId(post.getId());
            return PostDto.builder()
                    .id(post.getId())
                    .user(post.getUser())
                    .name(post.getName())
                    .imageURL(post.getImageURL())
                    .exhibitionURL(post.getExhibitionURL())
                    .description(post.getDescription())
                    .postType(post.getPostType())
                    .postDate(PostDateDto.from(post.getPostDate()))
                    .archived(archiveCount)
                    .build();
        });

    }
}
