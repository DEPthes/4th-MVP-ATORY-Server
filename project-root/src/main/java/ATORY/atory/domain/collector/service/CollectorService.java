package ATORY.atory.domain.collector.service;

import ATORY.atory.domain.collector.dto.CollectorDto;
import ATORY.atory.domain.collector.dto.CollectorWithPostDto;
import ATORY.atory.domain.collector.entity.Collector;
import ATORY.atory.domain.collector.repository.CollectorRepository;
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.service.PostService;
import ATORY.atory.domain.tag.service.TagQueryService;
import ATORY.atory.domain.user.dto.UserDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ATORY.atory.global.exception.UserNotFoundException;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class CollectorService {

    private final CollectorRepository collectorRepository;
    private final UserService userService;
    private final PostService postService;
    private final TagQueryService tagQueryService;

    private Collector findByIdOrThrow(Long id) {
        return collectorRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("COLLECTOR_NOT_FOUND"));
    }


    public CollectorDto findCollectorById(Long id) {
        Collector c = findByIdOrThrow(id);
        UserDto userDto = userService.findById(c.getUser().getId());

        return CollectorDto.builder()
                .id(c.getId())
                .user(userDto)
                .birth(c.getBirth())
                .educationBackground(c.getEducationBackground())
                .disclosureStatus(c.getDisclosureStatus())
                .notes(Collections.emptyList())
                .build();
    }

    public CollectorWithPostDto findPostByCollector(Long id, String postType, Pageable pageable) {
        Collector c = findByIdOrThrow(id);
        User user = c.getUser();

        UserDto userDto = userService.findById(user.getId());
        Page<PostDto> posts = postService.findPostsByUserIdAndPostType(user.getId(), postType, pageable);

        return CollectorWithPostDto.builder()
                .id(c.getId())
                .user(userDto)
                .birth(c.getBirth())
                .educationBackground(c.getEducationBackground())
                .disclosureStatus(c.getDisclosureStatus())
                .post(posts)
                .build();
    }

    public Object findTagsByCollectorAndType(Long id, String postType) {
        Collector c = findByIdOrThrow(id);
        return tagQueryService.findTagsByUserAndPostType(c.getUser().getId(), postType);
    }
}