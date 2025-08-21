package ATORY.atory.domain.user.service;

import ATORY.atory.domain.artist.dto.ArtistDetailDto;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.collector.dto.CollectorDetailDto;
import ATORY.atory.domain.collector.entity.Collector;
import ATORY.atory.domain.collector.repository.CollectorRepository;
import ATORY.atory.domain.follow.repository.FollowRepository;
import ATORY.atory.domain.gallery.dto.GalleryDetailDto;
import ATORY.atory.domain.gallery.entity.Gallery;
import ATORY.atory.domain.gallery.repository.GalleryRepository;
import ATORY.atory.domain.user.dto.ProfileDetailDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.global.dto.UserType;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailService {
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final FollowRepository followRepository;
    private final CollectorRepository collectorRepository;
    private final GalleryRepository galleryRepository;
    private final ObjectMapper objectMapper;

    private void fillCommonUserFields(User targetUser, User requester, ProfileDetailDto dto) throws JsonProcessingException {
        // profile URL
        List<String> profileUrls = new ArrayList<>();
        if (targetUser.getProfileImageURL() != null && !targetUser.getProfileImageURL().isBlank()) {
            profileUrls = objectMapper.readValue(targetUser.getProfileImageURL(),
                    new TypeReference<List<String>>() {});
        }

        // cover URL
        List<String> coverUrls = new ArrayList<>();
        if (targetUser.getCoverImageURL() != null && !targetUser.getCoverImageURL().isBlank()) {
            coverUrls = objectMapper.readValue(targetUser.getCoverImageURL(),
                    new TypeReference<List<String>>() {});
        }

        // 공통 세팅
        dto.setName(targetUser.getUsername());
        dto.setUserType(targetUser.getUserType());
        dto.setProfileImageUrl(profileUrls.isEmpty() ? null : profileUrls.get(0));
        dto.setCoverImageUrl(coverUrls.isEmpty() ? null : coverUrls.get(0));
        dto.setFollowersCount(followRepository.countFollowers(targetUser));
        dto.setFollowingCount(followRepository.countFollowing(targetUser));
        dto.setDescription(targetUser.getIntroduction());
        dto.setContact(targetUser.getContact());
        dto.setEmail(targetUser.getEmail());

        dto.setIsMe(targetUser.getId().equals(requester.getId()));
    }

    //아티스트 유저 정보 조회
    public ArtistDetailDto loadArtistDetail(String googleID, Long userId) throws JsonProcessingException {
        User targetUser = userRepository.findById(userId).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        Artist artist = artistRepository.findByUser(targetUser);
        User requester = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        ArtistDetailDto dto = new ArtistDetailDto();
        fillCommonUserFields(targetUser, requester, dto);

        dto.setArtistID(artist.getId());
        dto.setBirth(artist.getBirth());
        dto.setEducationBackground(artist.getEducationBackground());
        dto.setDisclosureStatus(artist.getDisclosureStatus());
        dto.setIsFollowed(followRepository.existsByFollower_IdAndFollowing_Id(requester.getId(), artist.getUser().getId()));

        return dto;
    }

    //콜렉터 유저 정보 조회
    public CollectorDetailDto loadCollectorDetail(String googleID, Long userId) throws JsonProcessingException {
        User targetUser = userRepository.findById(userId).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        Collector collector = collectorRepository.findByUser(targetUser);
        User requester = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        CollectorDetailDto dto = new CollectorDetailDto();
        fillCommonUserFields(targetUser, requester, dto);

        dto.setCollectorID(collector.getId());
        dto.setBirth(collector.getBirth());
        dto.setEducationBackground(collector.getEducationBackground());
        dto.setDisclosureStatus(collector.getDisclosureStatus());
        dto.setIsFollowed(followRepository.existsByFollower_IdAndFollowing_Id(requester.getId(), collector.getUser().getId()));

        return dto;
    }

    //갤러리 유저 정보 조회
    public GalleryDetailDto loadGalleryDetail(String googleID, Long userId) throws JsonProcessingException {
        User targetUser = userRepository.findById(userId).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        Gallery gallery = galleryRepository.findByUser(targetUser);
        User requester = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        GalleryDetailDto dto = new GalleryDetailDto();
        fillCommonUserFields(targetUser, requester, dto);

        dto.setGalleryID(gallery.getId());
        dto.setGalleryName(gallery.getName());
        dto.setLocation(gallery.getLocation());
        dto.setIsFollowed(followRepository.existsByFollower_IdAndFollowing_Id(requester.getId(), gallery.getUser().getId()));

        return dto;
    }

    public ProfileDetailDto loadProfile(String googleID, Long userId) throws JsonProcessingException {
        User targetUser = userRepository.findById(userId).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        if (targetUser.getUserType() == UserType.ARTIST) {
            return loadArtistDetail(googleID, userId);
        } else if (targetUser.getUserType() == UserType.COLLECTOR) {
            return loadCollectorDetail(googleID, userId);
        } else if (targetUser.getUserType() == UserType.GALLERY) {
            return loadGalleryDetail(googleID, userId);
        } else {
            throw new IllegalArgumentException("지원하지 않는 유저 타입");
        }
    }
}
