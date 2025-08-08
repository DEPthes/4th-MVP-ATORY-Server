package ATORY.atory.domain.artist.artistNote.service;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteListResponseDto;
import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDetailResponseDto;
import ATORY.atory.domain.artist.artistNote.repository.ArtistNoteRepository;
import ATORY.atory.domain.artist.entity.YouthArtist;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.global.exception.ArtistNotFoundException;
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
            Page<YouthArtist> youthArtists = artistNoteRepository.findArtistsWithNotes(pageable, PostType.ARTIST_NOTE);
            
            // DTO로 변환
            List<ArtistNoteListResponseDto> responseList = youthArtists.getContent().stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            
            // Page 객체 생성 (페이징 정보 유지)
            return new org.springframework.data.domain.PageImpl<>(
                    responseList,
                    pageable,
                    youthArtists.getTotalElements()
            );
            
        } catch (Exception e) {
            log.error("작가노트 리스트 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("작가노트 리스트 조회에 실패했습니다.", e);
        }
    }

    public ArtistNoteDetailResponseDto getArtistNoteDetail(Long noteId, Long currentUserId) {
        try {
            // 실제 구현에서는 Post 엔티티에서 작가노트 상세 정보를 조회
            // 현재는 YouthArtist 정보를 기반으로 상세 정보를 구성
            YouthArtist youthArtist = artistNoteRepository.findById(noteId)
                    .orElseThrow(() -> new ArtistNotFoundException("작가노트를 찾을 수 없습니다. ID: " + noteId));
            
            return convertToDetailResponseDto(youthArtist, noteId);
            
        } catch (Exception e) {
            log.error("작가노트 상세 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("작가노트 상세 조회에 실패했습니다.", e);
        }
    }

    private ArtistNoteListResponseDto convertToResponseDto(YouthArtist youthArtist) {
        User user = youthArtist.getUser();
        
        // 전화번호 마스킹 처리
        String maskedPhone = maskPhoneNumber(user.getPhone());
        
        // S3 이미지 URL 처리
        String profileImageUrl = getProfileImageUrl(user.getProfileImgUrl());
        
        return ArtistNoteListResponseDto.builder()
                .noteId(youthArtist.getId()) // 임시로 YouthArtist ID 사용
                .thumbnailUrl(profileImageUrl)
                .artist(ArtistNoteListResponseDto.ArtistInfo.builder()
                        .id(youthArtist.getId())
                        .nickname(user.getUsername())
                        .englishName("") // TODO: 영어 이름 필드 추가 필요
                        .job("ARTIST")
                        .phone(maskedPhone)
                        .email(user.getEmail())
                        .statusMessage(youthArtist.getStatusMessage())
                        .build())
                .build();
    }

    private ArtistNoteDetailResponseDto convertToDetailResponseDto(YouthArtist youthArtist, Long noteId) {
        User user = youthArtist.getUser();
        
        // 전화번호 마스킹 처리
        String maskedPhone = maskPhoneNumber(user.getPhone());
        
        // S3 이미지 URL 처리
        String profileImageUrl = getProfileImageUrl(user.getProfileImgUrl());
        
        return ArtistNoteDetailResponseDto.builder()
                .noteId(noteId)
                .title("작가노트 제목") // TODO: 실제 제목 필드 필요
                .content("작가노트 내용") // TODO: 실제 내용 필드 필요
                .imageUrls(List.of(profileImageUrl)) // TODO: 실제 이미지 URL 리스트 필요
                .createdAt(java.time.LocalDateTime.now()) // TODO: 실제 생성일시 필요
                .updatedAt(java.time.LocalDateTime.now()) // TODO: 실제 수정일시 필요
                .artist(ArtistNoteDetailResponseDto.ArtistProfileSummary.builder()
                        .id(youthArtist.getId())
                        .nickname(user.getUsername())
                        .englishName("") // TODO: 영어 이름 필드 추가 필요
                        .job("ARTIST")
                        .email(user.getEmail())
                        .phone(maskedPhone)
                        .statusMessage(youthArtist.getStatusMessage())
                        .profileImageUrl(profileImageUrl)
                        .build())
                .build();
    }

    /**
     * 전화번호 마스킹 처리
     * @param phone 원본 전화번호
     * @return 마스킹된 전화번호
     */
    private String maskPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return "";
        }
        
        // 숫자만 추출
        String numbers = phone.replaceAll("[^0-9]", "");
        
        if (numbers.length() == 11) {
            // 010-****-5678 형태로 마스킹
            return numbers.substring(0, 3) + "-****-" + numbers.substring(7);
        } else if (numbers.length() == 10) {
            // 02-****-5678 형태로 마스킹
            return numbers.substring(0, 2) + "-****-" + numbers.substring(6);
        } else {
            // 길이가 맞지 않으면 원본 반환
            return phone;
        }
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
