package ATORY.atory.domain.user.service;

import ATORY.atory.domain.follow.service.FollowService;

import ATORY.atory.domain.post.service.S3Service;
import ATORY.atory.domain.user.dto.UserDto;
import ATORY.atory.domain.user.dto.UserInfoSideDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final FollowService followService;
    private final S3Service s3Service;

    private final ObjectMapper objectMapper;

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
    public UserInfoSideDto loadUserSideInfo(String google_id){

        User user = userRepository.findByGoogleID(google_id).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        return UserInfoSideDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profileImageURL(user.getProfileImageURL())
                .userType(user.getUserType())
                .build();
    }

    //프로필 이미지 변경
    public Boolean changeProfileImage(String googleID, MultipartFile file) throws IOException {
        User user = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        if (user.getProfileImageURL() == null){
            String key = "uploads/" + UUID.randomUUID();
            String url = s3Service.upload(file, key);

            user.changeProfileImageURL(objectMapper.writeValueAsString(List.of(url)));
            userRepository.save(user);
        } else {
            List<String> urls = objectMapper.readValue(user.getCoverImageURL(), new TypeReference<List<String>>() {});
            String imageUrl = urls.get(0);

            String deleteKey = imageUrl.substring(imageUrl.indexOf("uploads/"));
            s3Service.delete(deleteKey);

            String key = "uploads/" + UUID.randomUUID();
            String url = s3Service.upload(file, key);

            user.changeProfileImageURL(objectMapper.writeValueAsString(List.of(url)));
            userRepository.save(user);
        }

        return true;
    }

    //커버 이미지 변경
    public Boolean changeCoverImage(String googleID, MultipartFile file) throws IOException {
        User user = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        if (user.getCoverImageURL() == null){
            String key = "uploads/" + UUID.randomUUID();
            String url = s3Service.upload(file, key);

            user.changeCoverImageURL(objectMapper.writeValueAsString(List.of(url)));
            userRepository.save(user);
        } else {
            List<String> urls = objectMapper.readValue(user.getCoverImageURL(), new TypeReference<List<String>>() {});
            String imageUrl = urls.get(0);

            String deleteKey = imageUrl.substring(imageUrl.indexOf("uploads/"));
            s3Service.delete(deleteKey);

            String key = "uploads/" + UUID.randomUUID();
            String url = s3Service.upload(file, key);

            user.changeCoverImageURL(objectMapper.writeValueAsString(List.of(url)));
            userRepository.save(user);
        }

        return true;
    }
}
