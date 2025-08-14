package ATORY.atory.domain.follow.service;

import ATORY.atory.domain.follow.repository.FollowRepository;
import ATORY.atory.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    //user를 팔로우 하고있는 사람들의 닉네임
    public List<String> findFollowers(User user) {
        return followRepository.findFollowerNicknames(user);
    }
    
    //user가 팔로우 하고있는 사람들의 닉네임
    public List<String> findFollowing(User user) {
        return followRepository.findFollowingNicknames(user);
    }
}
