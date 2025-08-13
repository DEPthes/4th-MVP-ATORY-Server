package ATORY.atory.domain.gallery.controller;

import ATORY.atory.domain.gallery.dto.GalleryDto;
import ATORY.atory.domain.gallery.dto.GalleryWithPostDto;
import ATORY.atory.domain.gallery.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    @GetMapping("/{id}")
    public GalleryDto findGalleryById(@PathVariable Long id) {
        return galleryService.findGalleryById(id);
    }

    @GetMapping("/{id}/posts")
    public GalleryWithPostDto findPostsByGallery(@PathVariable Long id,
                                                 @RequestParam("type") String postType,
                                                 Pageable pageable) {
        return galleryService.findPostByGallery(id, postType, pageable);
    }

    @GetMapping("/{id}/posts/tags")
    public Object findPostTags(@PathVariable Long id,
                               @RequestParam("type") String postType) {
        return galleryService.findTagsByGalleryAndType(id, postType);
    }
}