package ATORY.atory.domain.user.service;

import ATORY.atory.domain.follow.service.FollowService;

import ATORY.atory.domain.user.dto.UserDto;
import ATORY.atory.domain.user.dto.UserInfoSideDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final FollowService followService;

    public UserDto getById(Long Id){
        User user = userRepository.findById(Id).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        int follower =  followService.countFollower(user).intValue();
        int following = followService.countFollowing(user).intValue();

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .introduction(user.getIntroduction())
                .contact(user.getContact())
                .follower(follower)
                .following(following)
                .build();
    }

    //사이드 바 유저 정보 조회 로직
    public UserInfoSideDto loadUserSideInfo(long user_id, String google_id){

        User user = userRepository.findByGoogleID(google_id).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        //Google_id를 통해 찾은 user와 id 값 비교
        if (user.getId() != user_id){
            throw new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
        }

        return UserInfoSideDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profileImageURL(user.getProfileImageURL())
                .build();
    }
}
