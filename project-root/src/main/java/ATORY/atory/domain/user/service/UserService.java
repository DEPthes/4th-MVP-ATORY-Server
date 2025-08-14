package ATORY.atory.domain.user.service;

import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.collector.repository.CollectorRepository;
import ATORY.atory.domain.follow.service.FollowService;
import ATORY.atory.domain.gallery.repository.GalleryRepository;
import ATORY.atory.domain.user.dto.*;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.global.dto.UserType;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final FollowService followService;
    private final ArtistRepository artistRepository;
    private final CollectorRepository collectorRepository;
    private final GalleryRepository galleryRepository;

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

    public User getUserEntityById(Long Id){
        return userRepository.findById(Id).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
    }
    private boolean isLogin(CustomUserDetails loginUser) {
        return loginUser != null;
    }

    private boolean isOwner(User found,CustomUserDetails loginUser,boolean login) {
        if(login) {
            return found.getId().equals(loginUser.getUser().getId());
        }
        else {
            return false;
        }
    }
    public UserDto getUserById(Long Id,CustomUserDetails loginUser){
        User user = getUserEntityById(Id);
        int follower =  followService.countFollower(user).intValue();
        int following = followService.countFollowing(user).intValue();
        boolean login = isLogin(loginUser);
        boolean owner = isOwner(user,loginUser,login);
        UserType userType = getUserType(Id);
        List<String> followers = followService.findFollowers(user);
        List<String> followings = followService.findFollowing(user);

        UserDto baseDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .introduction(user.getIntroduction())
                .contact(user.getContact())
                .follower(follower)
                .following(following)
                .owner(owner)
                .login(login)
                .userType(userType)
                .followers(followers)
                .followings(followings)
                .build();

        if(userType == UserType.ARTIST) {
            UserWithArtistDto dto = new UserWithArtistDto();
            copyBaseFields(baseDto, dto);
            dto.setBirth(artistRepository.findBirthByUserId(Id));
            dto.setEducationBackground(artistRepository.findEducationBackgroundByUserId(Id));
            dto.setDisclosureStatus(artistRepository.findDisclosureStatusByUserId(Id));
            return dto;
        }
        else if(userType == UserType.GALLERY) {
            UserWithGalleryDto dto = new UserWithGalleryDto();
            copyBaseFields(baseDto, dto);
            dto.setLocation(galleryRepository.findLocationByUserId(Id));
            return dto;
        }
        else if(userType == UserType.COLLECTOR) {
            ArtCollectorDto dto = new ArtCollectorDto();
            copyBaseFields(baseDto, dto);
            dto.setBirth(collectorRepository.findBirthByUserId(Id));
            dto.setEducationBackground(collectorRepository.findEducationBackgroundByUserId(Id));
            dto.setDisclosureStatus(collectorRepository.findDisclosureStatusByUserId(Id));
            return dto;
        }

        return baseDto;
    }



    private void copyBaseFields(UserDto source, UserDto target) {
        target.setId(source.getId());
        target.setUsername(source.getUsername());
        target.setEmail(source.getEmail());
        target.setIntroduction(source.getIntroduction());
        target.setContact(source.getContact());
        target.setFollower(source.getFollower());
        target.setFollowing(source.getFollowing());
        target.setOwner(source.getOwner());
        target.setLogin(source.getLogin());
        target.setUserType(source.getUserType());
        target.setFollowers(source.getFollowers());
        target.setFollowings(source.getFollowings());
    }


    private UserType getUserType(Long Id){
        User user = getUserEntityById(Id);
        UserType userType;
        if (artistRepository.existsByUser_Id(user.getId())) {
            userType = UserType.ARTIST;
        } else if (collectorRepository.existsByUser_Id(user.getId())) {
            userType = UserType.COLLECTOR;
        } else if (galleryRepository.existsByUser_Id(user.getId())) {
            userType = UserType.GALLERY;
        } else {
            throw new MapperException(ErrorCode.SER_NOT_FOUND);
        }
        return userType;
    }
}
