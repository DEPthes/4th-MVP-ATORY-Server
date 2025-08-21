package ATORY.atory.domain.artist.artistNote.service;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteSaveDto;
import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import ATORY.atory.domain.artist.artistNote.repository.ArtistNoteRepository;
import ATORY.atory.domain.artist.dto.ArtistDto;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.artist.repository.ArtistRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistNoteService {

    private final ArtistNoteRepository artistNoteRepository;
    private final ArtistRepository artistRepository;
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

    //작가 노트 조회
    public List<ArtistNoteDto> loadArtistNote(String googleID, Long userID){
        User user = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        User artistUser = userRepository.findById(userID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        Artist artist = artistRepository.findByUser(artistUser);

        List<ArtistNote> artistNoteDtoList = artistNoteRepository.findByArtistId(artist.getId());
        List<ArtistNoteDto> result = new ArrayList<>();

        for (ArtistNote artistNote : artistNoteDtoList) {
            ArtistNoteDto artistNoteDto = new ArtistNoteDto();
            artistNoteDto.setId(artistNote.getId());
            artistNoteDto.setArtistNoteType(artistNote.getArtistNoteType());
            artistNoteDto.setYear(artistNote.getYear());
            artistNoteDto.setDescription(artistNote.getDescription());

            result.add(artistNoteDto);
        }

        return result;
    }

    //작가 노트 저장
    public Boolean saveArtistNote(ArtistNoteSaveDto artistNoteSaveDto, String googleID){
        User user = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        Artist artist = artistRepository.findByUser(user);

        artistNoteRepository.save(ArtistNote.builder()
                        .artist(artist)
                        .artistNoteType(artistNoteSaveDto.getArtistNoteType())
                        .year(artistNoteSaveDto.getYear())
                        .description(artistNoteSaveDto.getDescription())
                .build());

        return true;
    }

    //작가 노트 수정
    public Boolean updateArtistNote(Long artistNoteId, String googleID, ArtistNoteSaveDto artistNoteSaveDto){
        User user = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        Optional<ArtistNote> artistNote = artistNoteRepository.findById(artistNoteId);

        if (artistNote.isPresent()){
           ArtistNote artistNoteEntity = artistNote.get();
           artistNoteEntity.update(artistNoteSaveDto);
           artistNoteRepository.save(artistNoteEntity);
        }

        return true;
    }

    //작가 노트 삭제
    public Boolean deleteArtistNote(Long artistNoteId){
        Optional<ArtistNote> artistNote = artistNoteRepository.findById(artistNoteId);

        if (artistNote.isPresent()){
            artistNoteRepository.delete(artistNote.get());
        }

        return true;
    }
}
