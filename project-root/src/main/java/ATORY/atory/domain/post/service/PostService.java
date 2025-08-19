package ATORY.atory.domain.post.service;

import ATORY.atory.domain.archive.entity.Archive;
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

            List<String> imageUrls = new ArrayList<>();
            try {
                if (post.getImageURL() != null) {
                    imageUrls = objectMapper.readValue(
                            post.getImageURL(),
                            new TypeReference<List<String>>() {}
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException("이미지 URL JSON 파싱 실패", e);
            }

            List<String> url = new ArrayList<>();
            try {
                if (post.getExhibitionURL() != null) {
                    imageUrls = objectMapper.readValue(
                            post.getImageURL(),
                            new TypeReference<List<String>>() {}
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException("이미지 URL JSON 파싱 실패", e);
            }

            return PostDto.builder()
                    .id(post.getId())
                    .name(post.getName())
                    .imageURL(imageUrls)
                    .exhibitionURL(url)
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

            List<String> imageUrls = new ArrayList<>();
            try {
                if (post.getImageURL() != null) {
                    imageUrls = objectMapper.readValue(
                            post.getImageURL(),
                            new TypeReference<List<String>>() {}
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException("이미지 URL JSON 파싱 실패", e);
            }

            List<String> url = new ArrayList<>();
            try {
                if (post.getExhibitionURL() != null) {
                    imageUrls = objectMapper.readValue(
                            post.getImageURL(),
                            new TypeReference<List<String>>() {}
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException("이미지 URL JSON 파싱 실패", e);
            }

            return PostDto.builder()
                    .id(post.getId())
                    .name(post.getName())
                    .imageURL(imageUrls)
                    .exhibitionURL(url)
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

        //이미지 파일 업로드
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

    //메인 페이지 게시글 Dto 변환
    private PostListDto toDto(Post post, Long currentUserId) {
        PostListDto dto = new PostListDto();
        dto.setPostId(post.getId().toString());
        dto.setPostType(post.getPostType());
        dto.setTitle(post.getName());
        dto.setUserName(post.getUser().getUsername());
        dto.setIsArchived(archiveRepository.existsByUserIdAndPostId(currentUserId, post.getId()));

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

    //게시물 상세 페이지 조회
    public PostDto loadPost(Long postId, String googleID) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new MapperException(ErrorCode.INTERNAL_SERVER_ERROR));
        User currentUser = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        long archiveCount = archiveRepository.countByPostId(post.getId());

        //이미지 URL 불러오기
        List<String> imageUrls = new ArrayList<>();
        try {
            if (post.getImageURL() != null) {
                imageUrls = objectMapper.readValue(
                        post.getImageURL(),
                        new TypeReference<List<String>>() {}
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("이미지 URL JSON 파싱 실패", e);
        }

        //Exhibition URL 불러오기
        List<String> url = new ArrayList<>();
        try {
            if (post.getExhibitionURL() != null) {
                url = objectMapper.readValue(
                        post.getExhibitionURL(),
                        new TypeReference<List<String>>() {}
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("이미지 URL JSON 파싱 실패", e);
        }

        // 태그 불러오기
        List<TagDto> tags = tagService.getByPostId(post.getId())
                .stream()
                .map(tagPost -> TagDto.from(tagPost.getTag()))
                .toList();

        PostDto postDto = new PostDto();

        postDto.setPostType(post.getPostType());
        postDto.setId(post.getId());
        postDto.setDescription(post.getDescription());
        postDto.setArchived(archiveCount);
        postDto.setUserId(post.getUser().getId());
        postDto.setExhibitionURL(url);
        postDto.setName(post.getName());
        postDto.setImageURL(imageUrls);
        postDto.setUserType(post.getUser().getUserType());
        postDto.setPostDate(PostDateDto.from(postDateRepository.findByPostId(post.getId())));
        postDto.setIsMine(post.getUser().getGoogleID().equals(googleID));
        postDto.setIsArchived(archiveRepository.existsByUserIdAndPostId(currentUser.getId(), post.getId()));
        postDto.setTags(tags);

        return postDto;
    }

    //검색
    public Page<PostListDto> searchPosts(Pageable pageable, String googleID, String tag, PostType postType, String text) {
        User currentUser = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        Page<Post> posts;
        if (Objects.equals(tag, "ALL")) {
            posts = postRepository.searchAllOrderByCreatedAtDesc(postType, text, pageable);
        } else {
            posts = postRepository.searchByTagNameOrderByCreatedAtDesc(tag, postType, text, pageable);
        }

        return posts.map(post -> toDto(post, currentUser.getId()));
    }

    //게시물 삭제
    public Boolean deletePost(Long postId, String googleID) {
        User currentUser = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new MapperException(ErrorCode.INTERNAL_SERVER_ERROR));

        PostDate postDate = postDateRepository.findByPostId(postId);
        postDateRepository.delete(postDate);

        List<TagPost> tagPosts = tagPostRepository.findByPostId(postId);
        tagPostRepository.deleteAll(tagPosts);

        List<Archive> archives = archiveRepository.findByPostId(postId);
        archiveRepository.deleteAll(archives);

        postRepository.delete(post);

        return true;
    }

    //게시글 수정
    public Boolean changePost(Long postId, String googleID, PostSaveDto postDto) throws JsonProcessingException {
        User currentUser = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new MapperException(ErrorCode.INTERNAL_SERVER_ERROR));
        PostDate postDate = postDateRepository.findByPostId(postId);

        //이미지 URL 불러오기
        List<String> imageUrls = new ArrayList<>();
        try {
            if (post.getImageURL() != null) {
                imageUrls = objectMapper.readValue(
                        post.getImageURL(),
                        new TypeReference<List<String>>() {}
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("이미지 URL JSON 파싱 실패", e);
        }

        //저장된 이미지 삭제
        for (String imageUrl : imageUrls) {
            s3Service.delete(imageUrl);
        }

        List<String> uploadImageUrls = new ArrayList<>();

        //이미지 파일 업로드
        for (MultipartFile file : postDto.getImages()){
            try {
                String key = "uploads/" + UUID.randomUUID();
                String url = s3Service.upload(file, key);

                uploadImageUrls.add(url);

            } catch (IOException e){
                throw new RuntimeException("파일 업로드 실패: " + file.getOriginalFilename(), e);
            }
        }

        // List<String> → JSON
        String imageUrlsJson = objectMapper.writeValueAsString(uploadImageUrls);
        String exhibitionUrlJson = objectMapper.writeValueAsString(postDto.getUrl());

        //포스트 변경
        post.updatePost(postDto, imageUrlsJson, exhibitionUrlJson);
        postRepository.save(post);

        //포스트 데이트 수정시간 변경
        postDate.updateModifiedAt(LocalDateTime.now());
        postDateRepository.save(postDate);

        //게시물 태그 삭제
        List<TagPost> tagPosts = tagPostRepository.findByPostId(postId);
        tagPostRepository.deleteAll(tagPosts);

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
    public Page<PostListDto> loadPostsByUser(Pageable pageable, String googleID, String tag, PostType postType, Long userID) {
        User currentUser = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        Page<Post> posts;
        if (Objects.equals(tag, "ALL")) {
            posts = postRepository.findByUserOrderByCreatedAtDesc(userID, postType, pageable);
        } else {
            posts = postRepository.findByUserAndTagNameOrderByCreatedAtDesc(userID, tag, postType, pageable);
        }

        return posts.map(post -> toDto(post, currentUser.getId()));
    }
}
