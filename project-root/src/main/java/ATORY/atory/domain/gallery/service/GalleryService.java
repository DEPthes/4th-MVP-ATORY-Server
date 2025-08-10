package ATORY.atory.domain.gallery.service;

import ATORY.atory.domain.gallery.dto.GalleryDto;
import ATORY.atory.domain.gallery.dto.GalleryWithPostDto;
import ATORY.atory.domain.gallery.entity.Gallery;
import ATORY.atory.domain.gallery.repository.GalleryRepository; // ✅ 누락된 import 추가
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.service.PostService;
import ATORY.atory.domain.tag.service.TagQueryService;
import ATORY.atory.domain.user.dto.UserDto;
import ATORY.atory.domain.user.service.UserService;
import ATORY.atory.global.exception.UserNotFoundException;
import ATORY.atory.domain.archive.service.ArchiveService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository galleryRepository; // ✅ 필드 타입 매칭
    private final UserService userService;
    private final PostService postService;
    private final TagQueryService tagQueryService;
    private final ArchiveService archiveService;

    private Gallery findByIdOrThrow(Long id) {
        return galleryRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("GALLERY_NOT_FOUND"));
    }

    public GalleryDto findGalleryById(Long id) {
        Gallery g = findByIdOrThrow(id);
        UserDto userDto = userService.findById(g.getUser().getId());

        return GalleryDto.builder()
                .id(g.getId())
                .user(userDto)
                .name(g.getName())
                .location(g.getLocation())
                .registrationNumber(g.getRegistrationNumber())
                .notes(Collections.emptyList()) // 갤러리 노트 도메인이 아직 없으니 빈 리스트
                .build();
    }

    public GalleryWithPostDto findPostByGallery(Long id, String postType, Pageable pageable) {
        Gallery g = findByIdOrThrow(id);
        UserDto userDto = userService.findById(g.getUser().getId());

        String type = postType == null ? "" : postType.trim().toUpperCase();
        Page<PostDto> posts = postService.findPostsByUserIdAndPostType(
                g.getUser().getId(), type, pageable
        );

        return GalleryWithPostDto.builder()
                .id(g.getId())
                .user(userDto)
                .name(g.getName())
                .location(g.getLocation())
                .registrationNumber(g.getRegistrationNumber())
                .post(posts)
                .build();
    }

    public List<String> findTagsByGalleryAndType(Long id, String postType) {
        Gallery g = findByIdOrThrow(id);
        String type = postType == null ? "" : postType.trim().toUpperCase();
        return tagQueryService.findTagsByUserAndPostType(g.getUser().getId(), type);
    }

    public Page<PostDto> findArchivedByGallery(Long id, String postType, Pageable pageable) {
        Gallery g = findByIdOrThrow(id);
        String normalized = postType == null ? "" : postType.trim().toUpperCase();
        return archiveService.findArchivedPostsByUserAndType(g.getUser().getId(), normalized, pageable);
    }
}