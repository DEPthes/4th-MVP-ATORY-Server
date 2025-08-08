package ATORY.atory.domain.collector.service;

import ATORY.atory.domain.collector.dto.ArtCollectorProfileUpdateRequestDto;
import ATORY.atory.domain.collector.entity.ArtCollector;
import ATORY.atory.domain.collector.repository.ArtCollectorRepository;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArtCollectorService {

    private final ArtCollectorRepository artCollectorRepository;
    private final UserService userService;

    public ArtCollector updateProfile(Long userId, ArtCollectorProfileUpdateRequestDto requestDto) {
        User user = userService.findById(userId);
        
        ArtCollector artCollector = artCollectorRepository.findByUserId(userId);
        if (artCollector == null) {
            // 새로운 ArtCollector 엔티티 생성
            artCollector = ArtCollector.createArtCollector(
                    user,
                    requestDto.getBirthDate(),
                    requestDto.getEducationBackground(),
                    requestDto.getIsEducationPublic()
            );
        } else {
            // 기존 엔티티 업데이트
            artCollector.updateProfile(
                    requestDto.getBirthDate(),
                    requestDto.getEducationBackground(),
                    requestDto.getIsEducationPublic()
            );
        }

        return artCollectorRepository.save(artCollector);
    }

    public ArtCollector findByUserId(Long userId) {
        return artCollectorRepository.findByUserId(userId);
    }
} 