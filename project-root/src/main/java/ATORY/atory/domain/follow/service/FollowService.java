package ATORY.atory.domain.follow.service;

import ATORY.atory.domain.follow.repository.FollowRepository;
import ATORY.atory.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class FollowService {
    private final FollowRepository followRepository;

    //user를 팔로우 하고있는 사람의 수
    public Long countFollower(User user) {
        return followRepository.countFollowers(user);
    }

    //user가 팔로우 하고있는 사람의 수
    public Long countFollowing(User user) {
        return followRepository.countFollowing(user);
    }

}
