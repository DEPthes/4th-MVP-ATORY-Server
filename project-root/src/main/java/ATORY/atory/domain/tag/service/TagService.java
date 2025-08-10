package ATORY.atory.domain.tag.service;

import ATORY.atory.domain.tag.entity.Tag;
import ATORY.atory.domain.tag.entity.TagPost;
import ATORY.atory.domain.tag.repository.TagPostRepository;
import ATORY.atory.domain.tag.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagPostRepository tagPostRepository;
    private final TagRepository tagRepository;

    public List<TagPost> getByPostId(Long id) {
        return tagPostRepository.findByPostId(id);
    }


    public Optional<Tag> findByName(String name) {
        return tagRepository.findByName(name);
    }

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }
}
