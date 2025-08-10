package ATORY.atory.domain.tag.service;


import ATORY.atory.domain.tag.entity.TagPost;
import ATORY.atory.domain.tag.repository.TagPostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TagPostService {
    private final TagPostRepository tagPostRepository;

    public TagPost save(TagPost tagPost) {
        return tagPostRepository.save(tagPost);
    }
}
