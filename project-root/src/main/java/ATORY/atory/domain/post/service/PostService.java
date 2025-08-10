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
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    public PostRegisterDto updatePost(Long postId, PostRegisterDto postRegisterDto,
                                      List<MultipartFile> files,
                                      CustomUserDetails loginUser) throws JsonProcessingException {
        User user = loginUser.getUser();

        if(user==null)
        {
            throw new MapperException(ErrorCode.SER_NOT_FOUND);
        }

        Post post = getPost(postId)
                .orElseThrow(() -> new MapperException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new MapperException(ErrorCode.ACCESS_DENIED);
        }


        //image변경 여부 판단 로직
        boolean hasNewFiles = files != null && !files.isEmpty();
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> requestedUrls =  objectMapper.readValue(postRegisterDto.getImageURL(),
                new TypeReference<List<String>>() {}
        );
        List<String> urlsInDB = objectMapper.readValue(post.getImageURL(), new TypeReference<List<String>>() {});
        boolean existingUrlsChanged = !new HashSet<>(requestedUrls).equals(new HashSet<>(urlsInDB));

        if(hasNewFiles||existingUrlsChanged)
        {
            //image변경시 변경사항 반영 로직
            
            //삭제할 이미지 url 추출
            List<String> urlsToDelete = urlsInDB.stream()
                    .filter(url -> !requestedUrls.contains(url))
                    .toList();

            //S3에서 삭제
            for (String url : urlsToDelete) {
                s3ImageService.delete(url); // 삭제 메서드 필요
            }

            // 새 파일 업로드
            List<String> uploadedUrls = new ArrayList<>();
            if (hasNewFiles) {
                uploadedUrls = files.stream()
                        .map(s3ImageService::upload)
                        .collect(Collectors.toList());
            }

            // 최종 이미지 URL :  + 새 업로드 URL
            List<String> finalImageUrls = new ArrayList<>(requestedUrls);
            finalImageUrls.addAll(uploadedUrls);

            // JSON으로 변환 후 Post 엔티티에 저장
            String finalImageUrlJson = objectMapper.writeValueAsString(finalImageUrls);
            post.setImageURL(finalImageUrlJson);

        }
        //Post의 요소들을 요청으로 받은값으로 변경
        post.setName(postRegisterDto.getName());
        post.setExhibitionURL(postRegisterDto.getExhibitionURL());
        post.setDescription(postRegisterDto.getDescription());
        post.setPostType(postRegisterDto.getPostType());

        //PostDate의 수정시간 변경
        PostDate postDate = postDateRepository.findByPostId(post.getId());
        postDate.setModifiedAt(LocalDateTime.now());
        postDateRepository.save(postDate);

        //Tag를 입력받은 값으로 변경 및 사용되어지지않는 Tag삭제
        List<TagDto> tagPostInDB = tagPostService.findByPostId(postId).stream()
                .map(TagPost::getTag)
                .map(tag -> TagDto.from(tag))
                .toList();
        List<TagDto> requestedTagPost = postRegisterDto.getTags();
        boolean existingTagChanged = !new HashSet<>(tagPostInDB).equals(new HashSet<>(requestedTagPost));

        if(existingTagChanged)
        {
            List<TagDto> tagsToDelete = tagPostInDB.stream()
                    .filter(tag -> !requestedTagPost.contains(tag))
                    .toList();

            for (TagDto tagDto : tagsToDelete) {
                tagPostService.deleteByTagNameAndPostId(tagDto.getName(),postId);
            }

            List<TagDto> uploadedNewTags = requestedTagPost.stream()
                    .filter(tag -> !tagPostInDB.contains(tag))
                    .toList();

            for (TagDto tagDto : uploadedNewTags) {
                Tag tag = tagService.findByName(tagDto.getName())
                        .orElseGet(() -> tagService.save(
                                Tag.builder().name(tagDto.getName()).build()
                        ));

                TagPost tagPost = TagPost.builder()
                        .post(post)
                        .tag(tag)
                        .build();
                tagPostService.save(tagPost);
            }
        }

        return PostRegisterDto.from(post,PostDateDto.from(postDate),postRegisterDto.getTags());
    }

    public Optional<Post> getPost(Long postId) {
        return postRepository.findById(postId);
    }


    public void deletePost(Long postId, CustomUserDetails loginUser) throws JsonProcessingException {
        User user = loginUser.getUser();
        if(user==null)
        {
            throw new MapperException(ErrorCode.SER_NOT_FOUND);
        }
        Post post = getPost(postId).orElseThrow(() -> new MapperException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new MapperException(ErrorCode.ACCESS_DENIED);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> imageUrls = objectMapper.readValue(post.getImageURL(), new TypeReference<List<String>>() {});
        for (String url : imageUrls) {
            s3ImageService.delete(url);
        }

        List<TagPost> tagPosts = tagPostService.findByPostId(postId);
        for (TagPost tp : tagPosts) {
            tagPostService.deleteByTagNameAndPostId(tp.getTag().getName(), postId);
        }

        PostDate postDate = postDateRepository.findByPostId(postId);
        if (postDate != null) {
            postDateRepository.delete(postDate);
        }

        postRepository.delete(post);
    }
}
