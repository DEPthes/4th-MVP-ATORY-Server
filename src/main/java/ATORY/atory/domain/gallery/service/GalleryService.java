package ATORY.atory.domain.gallery.service;

import ATORY.atory.domain.gallery.dto.GalleryProfileUpdateRequestDto;
import ATORY.atory.domain.gallery.entity.Gallery;
import ATORY.atory.domain.gallery.repository.GalleryRepository;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryService {

    private final GalleryRepository galleryRepository;
    private final UserService userService;

    public Gallery updateProfile(Long userId, GalleryProfileUpdateRequestDto requestDto) {
        User user = userService.findById(userId);
        
        Gallery gallery = galleryRepository.findByUserId(userId);
        if (gallery == null) {
            // 새로운 Gallery 엔티티 생성
            gallery = Gallery.createGallery(
                    user,
                    requestDto.getName(),
                    requestDto.getLocation(),
                    requestDto.getRegistrationNumber()
            );
        } else {
            // 기존 엔티티 업데이트
            gallery.updateProfile(
                    requestDto.getName(),
                    requestDto.getLocation(),
                    requestDto.getRegistrationNumber()
            );
        }

        return galleryRepository.save(gallery);
    }

    public Gallery findByUserId(Long userId) {
        return galleryRepository.findByUserId(userId);
    }
}
