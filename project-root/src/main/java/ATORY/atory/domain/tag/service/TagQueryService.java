package ATORY.atory.domain.tag.service;

import ATORY.atory.domain.tag.repository.TagQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagQueryService {

    private final TagQueryRepository tagQueryRepository;

    public List<String> findTagsByUserAndPostType(Long userId, String postType) {
        return tagQueryRepository.findTagNamesByUserAndPostType(userId, postType);
    }
}
