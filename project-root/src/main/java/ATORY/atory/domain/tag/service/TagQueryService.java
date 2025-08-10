package ATORY.atory.domain.tag.service;

import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.tag.repository.TagQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagQueryService {

    private final TagQueryRepository tagQueryRepository;

    public List<String> findTagsByUserAndPostType(Long userId, PostType postType) {
        if (userId == null || postType == null) return Collections.emptyList();
        return tagQueryRepository.findTagNamesByUserAndPostType(userId, postType);
    }

    public List<String> findTagsByUserAndPostType(Long userId, String postType) {
        if (userId == null || postType == null || postType.isBlank()) return Collections.emptyList();
        try {
            PostType type = PostType.valueOf(postType.toUpperCase());
            return findTagsByUserAndPostType(userId, type);
        } catch (IllegalArgumentException e) {
            return tagQueryRepository.findTagNamesByUserAndPostType(userId, postType);
        }
    }
}
