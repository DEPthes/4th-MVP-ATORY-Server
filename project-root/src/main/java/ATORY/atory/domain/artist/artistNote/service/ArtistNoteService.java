package ATORY.atory.domain.artist.artistNote.service;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteListResponseDto;
import ATORY.atory.domain.artist.artistNote.repository.ArtistNoteRepository;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistNoteService {

    private final ArtistNoteRepository artistNoteRepository;
    
    // S3 기본 이미지 URL 
    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://atory-s3-bucket.s3.ap-northeast-2.amazonaws.com/default-profile.png";

    public Page<ArtistNoteListResponseDto> getArtistNoteList(Pageable pageable, Long currentUserId) {
        try {
            // 작가노트 게시물이 있는 작가들 조회
            Page<Artist> artists = artistNoteRepository.findArtistsWithNotes(pageable);
            
            // DTO로 변환
            List<ArtistNoteListResponseDto> responseList = artists.getContent().stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            
            // Page 객체 생성 (페이징 정보 유지)
            return new org.springframework.data.domain.PageImpl<>(
                    responseList,
                    pageable,
                    artists.getTotalElements()
            );
            
        } catch (Exception e) {
            log.error("작가노트 리스트 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("작가노트 리스트 조회에 실패했습니다.", e);
        }
    }

    private ArtistNoteListResponseDto convertToResponseDto(Artist artist) {
        User user = artist.getUser();
        
        // S3 이미지 URL 처리
        String profileImageUrl = getProfileImageUrl(user.getProfileImgUrl());
        
        return ArtistNoteListResponseDto.builder()
                .artistId(artist.getId())
                .nickname(user.getUsername())
                .job("작가") // 고정값
                .profileImageUrl(profileImageUrl)
                .bio(artist.getStatusMessage()) // 작가 상태메시지
                .build();
    }

    /**
     * S3 이미지 URL 처리
     * @param profileImgUrl 사용자 프로필 이미지 URL
     * @return 처리된 이미지 URL
     */
    private String getProfileImageUrl(String profileImgUrl) {
        if (profileImgUrl == null || profileImgUrl.trim().isEmpty()) {
            return DEFAULT_PROFILE_IMAGE_URL;
        }
        
        // S3 URL인지 확인 
        if (profileImgUrl.startsWith("https://atory-s3-bucket.s3.ap-northeast-2.amazonaws.com/")) {
            return profileImgUrl;
        }
        
        // 외부 URL인 경우 그대로 반환
        if (profileImgUrl.startsWith("http")) {
            return profileImgUrl;
        }
        
        // 상대 경로인 경우 S3 URL로 변환
        return "https://atory-s3-bucket.s3.ap-northeast-2.amazonaws.com/" + profileImgUrl;
    }
}
