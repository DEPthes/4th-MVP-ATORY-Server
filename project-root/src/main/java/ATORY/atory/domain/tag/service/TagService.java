package ATORY.atory.domain.tag.service;

import ATORY.atory.domain.tag.entity.TagPost;
import ATORY.atory.domain.tag.repository.TagPostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagPostRepository tagPostRepository;

    public List<TagPost> getByPostId(Long id) {
        return tagPostRepository.findByPostId(id);
    }
}
