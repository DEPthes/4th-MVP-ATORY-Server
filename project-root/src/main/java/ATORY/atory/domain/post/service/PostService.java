package ATORY.atory.domain.post.service;

import ATORY.atory.domain.archive.repository.ArchiveRepository;
import ATORY.atory.domain.archive.service.ArchiveService;
import ATORY.atory.domain.post.dto.PostDateDto;
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.dto.PostListDto;
import ATORY.atory.domain.post.dto.PostSaveDto;
import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.entity.PostDate;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.post.repository.PostDateRepository;
import ATORY.atory.domain.post.repository.PostRepository;
import ATORY.atory.domain.tag.dto.TagDto;
import ATORY.atory.domain.tag.entity.Tag;
import ATORY.atory.domain.tag.entity.TagPost;
import ATORY.atory.domain.tag.repository.TagPostRepository;
import ATORY.atory.domain.tag.repository.TagRepository;
import ATORY.atory.domain.tag.service.TagService;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.domain.user.service.UserService;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostDateRepository postDateRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TagPostRepository tagPostRepository;
    private final ArchiveRepository archiveRepository;
    private final ArchiveService archiveService;
    private final TagService tagService;
    private final S3Service s3Service;

    private final ObjectMapper objectMapper;

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

    //게시물 저장 로직
    public Boolean savePost(PostSaveDto postDto, String googleID) throws JsonProcessingException {

        User user = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : postDto.getImages()){
            try {
                String key = "uploads/" + UUID.randomUUID();
                String url = s3Service.upload(file, key);

                imageUrls.add(url);

            } catch (IOException e){
                throw new RuntimeException("파일 업로드 실패: " + file.getOriginalFilename(), e);
            }
        }

        // List<String> → JSON
        String imageUrlsJson = objectMapper.writeValueAsString(imageUrls);
        String exhibitionUrlJson = objectMapper.writeValueAsString(postDto.getUrl());

        //게시물 저장
        Post post = postRepository.save(Post.builder()
                        .postType(postDto.getPostType())
                        .description(postDto.getDescription())
                        .exhibitionURL(exhibitionUrlJson)
                        .user(user)
                        .name(postDto.getTitle())
                        .imageURL(imageUrlsJson)
                .build());

        //게시물 저장 시간 저장
        postDateRepository.save(PostDate.builder()
                        .post(post)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(null)
                .build());

        //게시물 태그 저장
        for (Long id : postDto.getTagIDs()){
            Tag tag = tagRepository.findById(id).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

            tagPostRepository.save(TagPost.builder()
                            .post(post)
                            .tag(tag)
                    .build());
        }

        return true;
    }

    //메인페이지 게시글 조회
    public Page<PostListDto> loadPosts(Pageable pageable, String googleID, String tag, PostType postType) {
        User currentUser = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        Page<Post> posts;
        if (Objects.equals(tag, "ALL")) {
            posts = postRepository.findAllOrderByCreatedAtDesc(postType, pageable);
        } else {
            posts = postRepository.findByTagNameOrderByCreatedAtDesc(tag, postType, pageable);
        }

        return posts.map(post -> toDto(post, currentUser.getId()));
    }

    private PostListDto toDto(Post post, Long currentUserId) {
        PostListDto dto = new PostListDto();
        dto.setPostId(post.getId().toString());
        dto.setPostType(post.getPostType());
        dto.setTitle(post.getName());
        dto.setUserName(post.getUser().getUsername());

        // 이미지 파싱
        try {
            if (post.getImageURL() != null) {
                List<String> urls = objectMapper.readValue(
                        post.getImageURL(),
                        new TypeReference<List<String>>() {}
                );
                dto.setImageUrls(urls);
            } else {
                dto.setImageUrls(List.of());
            }
        } catch (Exception e) {
            dto.setImageUrls(List.of());
        }

        // 아카이브 개수
        long archiveCount = archiveRepository.countByPostId(post.getId());
        dto.setArchived(archiveCount);

        // 내가 작성한 글인지 여부
        dto.setIsMine(post.getUser().getId().equals(currentUserId));

        return dto;
    }


}
