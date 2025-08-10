package ATORY.atory.domain.post.service;

import ATORY.atory.domain.archive.service.ArchiveService;
import ATORY.atory.domain.post.dto.PostDateDto;
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.dto.PostRegisterDto;
import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.tag.entity.TagPost;
import ATORY.atory.domain.post.entity.PostDate;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.post.repository.PostDateRepository;
import ATORY.atory.domain.post.repository.PostRepository;
import ATORY.atory.domain.tag.dto.TagDto;
import ATORY.atory.domain.tag.entity.Tag;
import ATORY.atory.domain.tag.service.TagPostService;
import ATORY.atory.domain.tag.service.TagService;
import ATORY.atory.domain.user.dto.CustomUserDetails;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import ATORY.atory.global.s3.S3ImageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final TagPostService tagPostService;
    private final S3ImageService s3ImageService;


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

    public PostRegisterDto createPost(PostRegisterDto postRegisterDto, List<MultipartFile> files, CustomUserDetails loginUser) throws JsonProcessingException {
        User user = loginUser.getUser();
        if(user==null)
        {
            throw new MapperException(ErrorCode.SER_NOT_FOUND);
        }

        List<String> uploadedUrls = files.stream()
                .map(s3ImageService::upload)
                .toList();

        ObjectMapper objectMapper = new ObjectMapper();
        String imageUrlJson = objectMapper.writeValueAsString(uploadedUrls);
        String exhibitionUrlJson = objectMapper.writeValueAsString(postRegisterDto.getExhibitionURL());

        Post post = Post.builder()
                .user(user)
                .name(postRegisterDto.getName())
                .imageURL(imageUrlJson)
                .exhibitionURL(exhibitionUrlJson)
                .description(postRegisterDto.getDescription())
                .postType(postRegisterDto.getPostType())
                .build();

        Post saved = postRepository.save(post);
        PostDate postDate = PostDate.builder()
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .post(post)
                .build();

        postDateRepository.save(postDate);
        List<Tag> savedTags = new ArrayList<>();


        if (postRegisterDto.getTags() != null && !postRegisterDto.getTags().isEmpty()) {
            for (TagDto tagDto : postRegisterDto.getTags()) {
                Tag tag = tagService.findByName(tagDto.getName())
                        .orElseGet(() -> tagService.save(
                                Tag.builder().name(tagDto.getName()).build()
                        ));

                TagPost tagPost = TagPost.builder()
                        .post(post)
                        .tag(tag)
                        .build();
                tagPostService.save(tagPost);
                savedTags.add(tag);
            }
        }




        return PostRegisterDto.from(saved,PostDateDto.from(postDate),TagDto.from(savedTags));
    }
}
