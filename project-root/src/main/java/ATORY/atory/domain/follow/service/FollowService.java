package ATORY.atory.domain.follow.service;

import ATORY.atory.domain.follow.dto.FollowToggleResponse;
import ATORY.atory.domain.follow.entity.Follow;
import ATORY.atory.domain.follow.repository.FollowRepository;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    //user를 팔로우 하고있는 사람의 수
    public Long countFollower(User user) {
        return followRepository.countFollowers(user);
    }

    //user가 팔로우 하고있는 사람의 수
    public Long countFollowing(User user) {
        return followRepository.countFollowing(user);
    }

    public FollowToggleResponse toggle(Long meId, Long id) {
        // 자기 자신 팔로우 방지
        if (meId.equals(id)) throw new MapperException(ErrorCode.SELF_FOLLOW_NOT_ALLOWED);

        // 대상 유저 존재 여부 확인
        if (!userRepository.existsById(id)) throw new MapperException(ErrorCode.SER_NOT_FOUND);

        // 현재 팔로우 상태 확인
        boolean existed = followRepository.existsByFollower_IdAndFollowing_Id(meId, id);

        if (existed) {
            // 이미 팔로우 상태 → 언팔
            followRepository.deleteByFollower_IdAndFollowing_Id(meId, id);
        } else {
            // 팔로우 (DB에서 엔티티 전체 SELECT 없이 프록시로 참조)
            User meRef = userRepository.getReferenceById(meId);
            User targetRef = userRepository.getReferenceById(id);
            try {
                followRepository.save(new Follow(meRef, targetRef));
            } catch (DataIntegrityViolationException e) {
                // 유니크 충돌 등: 이미 팔로우된 경우
                throw new MapperException(ErrorCode.FOLLOW_CONFLICT);
            }
        }

        // 대상id 최신 팔로워 수
        long targetFollowers = followRepository.countByFollowing_Id(id);

        return FollowToggleResponse.builder()
                .targetUserId(id)
                .isFollowing(!existed) // 토글 후 상태
                .targetFollowerCount(targetFollowers)
                .build();

    }
}
