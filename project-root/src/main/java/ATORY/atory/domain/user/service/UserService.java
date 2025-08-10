package ATORY.atory.domain.user.service;

import ATORY.atory.domain.follow.service.FollowService;

import ATORY.atory.domain.user.dto.BannerUpdateResponseDto;
import ATORY.atory.domain.user.dto.UserDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UseRepository;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UseRepository useRepository;
    private final FollowService followService;
    private final S3ImageService s3ImageService;

    public UserDto getById(Long Id){
        User user = useRepository.findById(Id).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
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

    public BannerUpdateResponseDto updateUserBanner(Long userId, MultipartFile file) {
        try {
            User user = useRepository.findById(userId)
                    .orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

            String uploadedUrl = s3ImageService.upload(file);

            user.setCoverImageURL(uploadedUrl);
            useRepository.save(user);

            return BannerUpdateResponseDto.builder()
                    .success(true)
                    .message("배너 이미지가 성공적으로 업데이트 되었습니다.")
                    .updatedBannerUrl(uploadedUrl)
                    .build();

        } catch (Exception e) {
            return BannerUpdateResponseDto.builder()
                    .success(false)
                    .message("배너 이미지 업데이트 실패: " + e.getMessage())
                    .updatedBannerUrl(null)
                    .build();
        }

    }
}
