package ATORY.atory.domain.artist.artistNote.service;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import ATORY.atory.domain.artist.artistNote.repository.ArtistNoteRepository;
import ATORY.atory.domain.artist.dto.ArtistDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.global.dto.UserType;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistNoteService {

    private final ArtistNoteRepository artistNoteRepository;
    private final UserRepository userRepository;

    public List<ArtistNote> getByArtistId(Long artistId){
        return artistNoteRepository.findByArtistId(artistId);
    }

    //메인 페이지 작가 노트 조회
    public Page<ArtistDto> loadArtistNotes(Pageable pageable, String googleID){
        User currentUser = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        Long userId = currentUser.getId();

        return userRepository.findTopByUserTypeOrderByFollowerCountDesc(UserType.ARTIST, pageable)
                .map(user -> new ArtistDto(
                        user.getId(),
                        user.getUsername(),
                        user.getContact(),
                        user.getIntroduction(),
                        user.getEmail(),
                        user.getProfileImageURL(),
                        user.getId().equals(userId)
                ));
    }


}
