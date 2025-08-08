package ATORY.atory.domain.post.service;

import ATORY.atory.domain.archive.service.ArchiveService;
import ATORY.atory.domain.post.dto.PostDateDto;
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.repository.PostDateRepository;
import ATORY.atory.domain.post.repository.PostRepository;
import ATORY.atory.domain.tag.dto.TagDto;
import ATORY.atory.domain.tag.entity.Tag;
import ATORY.atory.domain.tag.service.TagService;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostDateRepository postDateRepository;
    private final ArchiveService archiveService;
    private final TagService tagService;
    public Slice<PostDto> getPostsByUserId(Long id, String postType, Pageable pageable) {
        Slice<Post> posts = postRepository.findPostsByUserId(id,postType,pageable);
        if (posts.isEmpty()) {
            throw new MapperException(ErrorCode.SER_NOT_FOUND);
        }
        List<Tag> tags = posts.stream()
                .flatMap(post -> tagService.getByPostId(post.getId()).stream())
                .map(tagPost -> tagPost.getTag())
                .distinct()
                .toList();

        return posts.map(post -> {
            Long archiveCount = archiveService.getArchiveCountByPostId(post.getId());
            return PostDto.builder()
                    .id(post.getId())
                    .name(post.getName())
                    .imageURL(post.getImageURL())
                    .exhibitionURL(post.getExhibitionURL())
                    .description(post.getDescription())
                    .postType(post.getPostType())
                    .postDate(PostDateDto.from(postDateRepository.findByPostId(post.getId())))
                    .archived(archiveCount)
                    .tags(TagDto.from(tags))
                    .build();
        });

    }

    public Slice<PostDto> getPostsByUserIdAndTag(Long id, String postType,String tag, Pageable pageable) {
        Slice<Post> posts = postRepository.findPostsByUserIdAndTag(id,postType,tag,pageable);
        if (posts.isEmpty()) {
            throw new MapperException(ErrorCode.SER_NOT_FOUND);
        }
        List<Tag> tags = posts.stream()
                .flatMap(post -> tagService.getByPostId(post.getId()).stream())
                .map(tagPost -> tagPost.getTag())
                .distinct()
                .toList();


        return posts.map(post -> {
            Long archiveCount = archiveService.getArchiveCountByPostId(post.getId());
            return PostDto.builder()
                    .id(post.getId())
                    .name(post.getName())
                    .imageURL(post.getImageURL())
                    .exhibitionURL(post.getExhibitionURL())
                    .description(post.getDescription())
                    .postType(post.getPostType())
                    .postDate(PostDateDto.from(postDateRepository.findByPostId(post.getId())))
                    .archived(archiveCount)
                    .tags(TagDto.from(tags))
                    .build();
        });
    }
}
