package ATORY.atory.domain.post.service;

import ATORY.atory.domain.archive.service.ArchiveService;
import ATORY.atory.domain.post.dto.PostDateDto;
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.entity.PostType;
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

    private List<Tag> findAllTags(Slice<Post> posts ) {
        return posts.stream()
                .flatMap(post -> tagService.getByPostId(post.getId()).stream())
                .map(tagPost -> tagPost.getTag())
                .distinct()
                .toList();

    }

    public Slice<PostDto> getPostsByUserId(Long id, PostType postType, Pageable pageable) {
        Slice<Post> posts = postRepository.findPostsByUserId(id,postType,pageable);

        List<Tag> tags = findAllTags(posts);

        return posts.map(post -> {
            Long archiveCount = archiveService.getArchiveCountByPostId(post.getId());
            PostDateDto postDateDto = PostDateDto.from(postDateRepository.findByPostId(post.getId()));
            List<TagDto> tagDtos =TagDto.from(tags);
            return PostDto.from(post,archiveCount,postDateDto,tagDtos);
        });
    }

    public Slice<PostDto> getPostsByUserIdAndTag(Long id, PostType postType,String tag, Pageable pageable) {
        Slice<Post> posts = postRepository.findPostsByUserIdAndTag(id,postType,tag,pageable);

        List<Tag> tags = findAllTags(posts);

        return posts.map(post -> {
            Long archiveCount = archiveService.getArchiveCountByPostId(post.getId());
            PostDateDto postDateDto = PostDateDto.from(postDateRepository.findByPostId(post.getId()));
            List<TagDto> tagDtos =TagDto.from(tags);
            return PostDto.from(post,archiveCount,postDateDto,tagDtos);
        });
    }

    public Slice<PostDto> getArchiveByUserId(Long id, Pageable pageable) {
        Slice<Post> posts = postRepository.findArchiveByUserId(id,pageable);
        List<Tag> tags = findAllTags(posts);

        return posts.map(post -> {
            Long archiveCount = archiveService.getArchiveCountByPostId(post.getId());
            PostDateDto postDateDto = PostDateDto.from(postDateRepository.findByPostId(post.getId()));
            List<TagDto> tagDtos =TagDto.from(tags);
            return PostDto.from(post,archiveCount,postDateDto,tagDtos);
        });
    }

    public Slice<PostDto> getArchiveByUserIdAndType(Long id, PostType postType, Pageable pageable) {
        Slice<Post> posts = postRepository.findArchiveByUserIdAndPostType(id,postType,pageable);
        List<Tag> tags = findAllTags(posts);

        return posts.map(post -> {
            Long archiveCount = archiveService.getArchiveCountByPostId(post.getId());
            PostDateDto postDateDto = PostDateDto.from(postDateRepository.findByPostId(post.getId()));
            List<TagDto> tagDtos =TagDto.from(tags);
            return PostDto.from(post,archiveCount,postDateDto,tagDtos);
        });

    }
}
