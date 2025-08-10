package ATORY.atory.domain.archive.service;

import ATORY.atory.domain.archive.repository.ArchiveRepository;
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.entity.PostType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ArchiveService {

    private final ArchiveRepository archiveRepository;

    public Long getArchiveCountByPostId(Long postId) {
        return archiveRepository.countByPostId(postId);
    }

    public Page<PostDto> findArchivedPostsByUserAndType(Long userId, String postType, Pageable pageable) {
        Page<Post> page;
        if (postType == null || postType.isBlank()) {
            page = archiveRepository.findArchivedPostsByUserId(userId, pageable);
        } else {
            PostType type = PostType.valueOf(postType.trim().toUpperCase());
            page = archiveRepository.findArchivedPostsByUserIdAndType(userId, type, pageable);
        }
        return page.map(this::toDto);
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