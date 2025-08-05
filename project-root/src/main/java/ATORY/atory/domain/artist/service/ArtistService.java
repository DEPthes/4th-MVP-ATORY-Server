package ATORY.atory.domain.artist.service;

import ATORY.atory.domain.artist.dto.ArtistProfileResponseDto;
import ATORY.atory.domain.artist.dto.ArtistNoteExistsResponseDto;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.post.repository.PostRepository;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.global.exception.ArtistNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final PostRepository postRepository;

    public ArtistProfileResponseDto getArtistProfile(Long authorId, Long currentUserId) {
        Artist artist = artistRepository.findById(authorId)
                .orElseThrow(() -> new ArtistNotFoundException("작가를 찾을 수 없습니다. ID: " + authorId));

        User user = artist.getUser();
        
        // 작가노트 게시물 수 확인
        long noteCount = postRepository.countByUserIdAndType(user.getId(), "ARTIST_NOTE");
        boolean hasNotes = noteCount > 0;

        // 로그인 여부에 따른 연락처/이메일 공개 처리
        boolean isLoggedIn = currentUserId != null;
        boolean isOwnProfile = isLoggedIn && currentUserId.equals(user.getId());
        
        String email = null;
        String contact = null;
        
        if (isOwnProfile || artist.getIsEmailPublic()) {
            email = user.getEmail();
        }
        
        if (isOwnProfile || artist.getIsContactPublic()) {
            contact = user.getPhone();
        }

        return ArtistProfileResponseDto.builder()
                .authorId(artist.getId())
                .nickname(user.getUsername())
                .profileImageUrl(user.getProfileImgUrl())
                .profession("작가")
                .statusMessage(artist.getStatusMessage())
                .email(email)
                .contact(contact)
                .bannerImageUrl(artist.getBannerImageUrl())
                .hasNotes(hasNotes)
                .englishName(artist.getEnglishName())
                .isContactPublic(artist.getIsContactPublic())
                .isEmailPublic(artist.getIsEmailPublic())
                .build();
    }

    public ArtistNoteExistsResponseDto checkArtistNoteExists(Long authorId) {
        Artist artist = artistRepository.findById(authorId)
                .orElseThrow(() -> new ArtistNotFoundException("작가를 찾을 수 없습니다. ID: " + authorId));

        User user = artist.getUser();
        long noteCount = postRepository.countByUserIdAndType(user.getId(), "ARTIST_NOTE");
        boolean exists = noteCount > 0;

        return new ArtistNoteExistsResponseDto(authorId, exists, (int) noteCount);
    }
}
