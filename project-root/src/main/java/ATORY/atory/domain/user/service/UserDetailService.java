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
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
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

    //아티스트 유저 정보 조회
    public ArtistDetailDto loadArtistDetail(String googleID, Long UserID) throws JsonProcessingException {
        User artistUser = userRepository.findById(UserID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        Artist artist = artistRepository.findByUser(artistUser);
        User user = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        List<String> profileUrls = new ArrayList<>();
        if (artistUser.getProfileImageURL() != null && !artistUser.getProfileImageURL().isBlank()) {
            profileUrls = objectMapper.readValue(
                    artistUser.getProfileImageURL(),
                    new TypeReference<List<String>>() {}
            );
        }

        List<String> coverUrls = new ArrayList<>();
        if (artistUser.getCoverImageURL() != null && !artistUser.getCoverImageURL().isBlank()) {
            coverUrls = objectMapper.readValue(
                    artistUser.getCoverImageURL(),
                    new TypeReference<List<String>>() {}
            );
        }

        ArtistDetailDto artistDetailDto = new ArtistDetailDto();
        artistDetailDto.setArtistID(artist.getId());
        artistDetailDto.setName(artistUser.getUsername());
        artistDetailDto.setUserType(artistUser.getUserType());
        artistDetailDto.setProfileImageUrl(profileUrls.isEmpty() ? null : profileUrls.get(0));
        artistDetailDto.setCoverImageUrl(coverUrls.isEmpty() ? null : coverUrls.get(0));
        artistDetailDto.setFollowersCount(followRepository.countFollowers(artistUser));
        artistDetailDto.setFollowingCount(followRepository.countFollowing(artistUser));
        artistDetailDto.setDescription(artistUser.getIntroduction());
        artistDetailDto.setBirth(artist.getBirth());
        artistDetailDto.setContact(artistUser.getContact());
        artistDetailDto.setEducationBackground(artist.getEducationBackground());
        artistDetailDto.setEmail(artistUser.getEmail());

        artistDetailDto.setIsMe(artist.getUser().equals(user));
        artistDetailDto.setIsFollowed(followRepository.existsByFollower_IdAndFollowing_Id(user.getId(), artist.getId()));
        artistDetailDto.setDisclosureStatus(artist.getDisclosureStatus());

        return artistDetailDto;
    }

    //콜렉터 유저 정보 조회
    public CollectorDetailDto loadCollectorDetail(String googleID, Long UserID) throws JsonProcessingException {
        User collectUser = userRepository.findById(UserID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        Collector collector = collectorRepository.findByUser(collectUser);
        User user = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        List<String> profileUrls = new ArrayList<>();
        if (collectUser.getProfileImageURL() != null && !collectUser.getProfileImageURL().isBlank()) {
            profileUrls = objectMapper.readValue(
                    collectUser.getProfileImageURL(),
                    new TypeReference<List<String>>() {}
            );
        }

        List<String> coverUrls = new ArrayList<>();
        if (collectUser.getCoverImageURL() != null && !collectUser.getCoverImageURL().isBlank()) {
            coverUrls = objectMapper.readValue(
                    collectUser.getCoverImageURL(),
                    new TypeReference<List<String>>() {}
            );
        }

        CollectorDetailDto collectorDetailDto = new CollectorDetailDto();
        collectorDetailDto.setCollectorID(collector.getId());
        collectorDetailDto.setName(collectUser.getUsername());
        collectorDetailDto.setUserType(collectUser.getUserType());
        collectorDetailDto.setProfileImageUrl(profileUrls.isEmpty() ? null : profileUrls.get(0));
        collectorDetailDto.setCoverImageUrl(coverUrls.isEmpty() ? null : coverUrls.get(0));
        collectorDetailDto.setFollowersCount(followRepository.countFollowers(collectUser));
        collectorDetailDto.setFollowingCount(followRepository.countFollowing(collectUser));
        collectorDetailDto.setDescription(collectUser.getIntroduction());
        collectorDetailDto.setBirth(collector.getBirth());
        collectorDetailDto.setContact(collectUser.getContact());
        collectorDetailDto.setEducationBackground(collector.getEducationBackground());
        collectorDetailDto.setEmail(collectUser.getEmail());

        collectorDetailDto.setIsMe(collector.getUser().equals(user));
        collectorDetailDto.setIsFollowed(followRepository.existsByFollower_IdAndFollowing_Id(user.getId(), collector.getId()));
        collectorDetailDto.setDisclosureStatus(collector.getDisclosureStatus());

        return collectorDetailDto;
    }

    //갤러리 유저 정보 조회
    public GalleryDetailDto loadGalleryDetail(String googleID, Long UserID) throws JsonProcessingException {
        User galleryUser = userRepository.findById(UserID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        Gallery gallery = galleryRepository.findByUser(galleryUser);
        User user = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        List<String> profileUrls = new ArrayList<>();
        if (galleryUser.getProfileImageURL() != null && !galleryUser.getProfileImageURL().isBlank()) {
            profileUrls = objectMapper.readValue(
                    galleryUser.getProfileImageURL(),
                    new TypeReference<List<String>>() {}
            );
        }

        List<String> coverUrls = new ArrayList<>();
        if (galleryUser.getCoverImageURL() != null && !galleryUser.getCoverImageURL().isBlank()) {
            coverUrls = objectMapper.readValue(
                    galleryUser.getCoverImageURL(),
                    new TypeReference<List<String>>() {}
            );
        }

        GalleryDetailDto galleryDetailDto = new GalleryDetailDto();
        galleryDetailDto.setGalleryID(gallery.getId());
        galleryDetailDto.setName(galleryUser.getUsername());
        galleryDetailDto.setUserType(galleryUser.getUserType());
        galleryDetailDto.setProfileImageUrl(profileUrls.isEmpty() ? null : profileUrls.get(0));
        galleryDetailDto.setCoverImageUrl(coverUrls.isEmpty() ? null : coverUrls.get(0));
        galleryDetailDto.setFollowersCount(followRepository.countFollowers(galleryUser));
        galleryDetailDto.setFollowingCount(followRepository.countFollowing(galleryUser));
        galleryDetailDto.setDescription(galleryUser.getIntroduction());
        galleryDetailDto.setLocation(gallery.getLocation());
        galleryDetailDto.setContact(galleryUser.getContact());
        galleryDetailDto.setGalleryName(gallery.getName());
        galleryDetailDto.setEmail(galleryUser.getEmail());

        galleryDetailDto.setIsMe(gallery.getUser().equals(user));
        galleryDetailDto.setIsFollowed(followRepository.existsByFollower_IdAndFollowing_Id(user.getId(), gallery.getId()));

        return galleryDetailDto;
    }
}
