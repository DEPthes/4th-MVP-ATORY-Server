package ATORY.atory.domain.tag.service;

import ATORY.atory.domain.tag.dto.TagDto;
import ATORY.atory.domain.tag.entity.Tag;
import ATORY.atory.domain.tag.entity.TagPost;
import ATORY.atory.domain.tag.repository.TagPostRepository;
import ATORY.atory.domain.tag.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagPostRepository tagPostRepository;
    private final TagRepository tagRepository;

    public List<TagPost> getByPostId(Long id) {
        return tagPostRepository.findByPostId(id);
    }

    public List<TagDto> loadTags() {
        List<Tag> tags = tagRepository.findAll();
        List<TagDto> tagDtos = new ArrayList<>();

        for (Tag tag : tags) {
            TagDto tagDto = new TagDto();
            tagDto.setId(tag.getId());
            tagDto.setName(tag.getName());
            tagDtos.add(tagDto);
        }

        return tagDtos;
    }
}
