package ATORY.atory.domain.artist.service;

import ATORY.atory.domain.artist.dto.YouthArtistProfileUpdateRequestDto;
import ATORY.atory.domain.artist.entity.YouthArtist;
import ATORY.atory.domain.artist.repository.YouthArtistRepository;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.service.UserService;
import ATORY.atory.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class YouthArtistService {

    private final YouthArtistRepository youthArtistRepository;
    private final UserService userService;

    public YouthArtist updateProfile(Long userId, YouthArtistProfileUpdateRequestDto requestDto) {
        User user = userService.findById(userId);
        
        YouthArtist youthArtist = youthArtistRepository.findByUserId(userId);
        if (youthArtist == null) {
            // 새로운 YouthArtist 엔티티 생성
            youthArtist = YouthArtist.createYouthArtist(
                    user,
                    requestDto.getBirthDate(),
                    requestDto.getEducationBackground(),
                    requestDto.getIsEducationPublic()
            );
        } else {
            // 기존 엔티티 업데이트
            youthArtist.updateProfile(
                    requestDto.getBirthDate(),
                    requestDto.getEducationBackground(),
                    requestDto.getIsEducationPublic()
            );
        }

        return youthArtistRepository.save(youthArtist);
    }

    public YouthArtist findByUserId(Long userId) {
        return youthArtistRepository.findByUserId(userId);
    }
} 