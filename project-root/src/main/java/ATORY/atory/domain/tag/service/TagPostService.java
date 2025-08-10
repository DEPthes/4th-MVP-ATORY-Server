package ATORY.atory.domain.tag.service;


import ATORY.atory.domain.tag.entity.Tag;
import ATORY.atory.domain.tag.entity.TagPost;
import ATORY.atory.domain.tag.repository.TagPostRepository;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagPostService {
    private final TagPostRepository tagPostRepository;
    private final TagService tagService;

    public TagPost save(TagPost tagPost) {
        return tagPostRepository.save(tagPost);
    }

    public List<TagPost> findByPostId(Long postId) {
        return tagPostRepository.findByPostId(postId);
    }

    public void deleteByTagNameAndPostId(String name, Long postId) {
        Tag tag = tagService.findByName(name)
                .orElseThrow(() -> new MapperException(ErrorCode.TAG_NOT_FOUND));

        tagPostRepository.deleteByTagNameAndPostId(name,postId);
        long usageCount = tagPostRepository.countByTag(tag);
        if (usageCount == 0) {
            tagService.delete(tag); // 태그 삭제
        }

    }
}
