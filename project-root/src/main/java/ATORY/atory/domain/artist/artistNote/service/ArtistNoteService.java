package ATORY.atory.domain.artist.artistNote.service;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import ATORY.atory.domain.artist.artistNote.repository.ArtistNoteRepository;
import ATORY.atory.domain.artist.service.ArtistService;
import ATORY.atory.domain.user.dto.CustomUserDetails;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistNoteService {

    private final ArtistNoteRepository artistNoteRepository;
    private final ArtistService artistService;

    public ArtistNoteDto create(ArtistNoteDto dto, CustomUserDetails loginUser) {

        if (loginUser == null || loginUser.getUser() == null) {
            throw new MapperException(ErrorCode.SER_NOT_FOUND);
        }

        User user = loginUser.getUser();

        ArtistNote artistNote = ArtistNote.builder()
                .artist(artistService.getArtistByUserId(user.getId()))
                .artistNoteType(dto.getArtistNoteType())
                .year(dto.getYear())
                .description(dto.getDescription())
                .build();

        ArtistNote saved = artistNoteRepository.save(artistNote);

        return ArtistNoteDto.from(saved);

    }

    public List<ArtistNoteDto> updateMultiple(List<ArtistNoteDto> notes, CustomUserDetails loginUser) {
        if (loginUser == null || loginUser.getUser() == null) {
            throw new MapperException(ErrorCode.SER_NOT_FOUND);
        }

        User user = loginUser.getUser();
        List<ArtistNoteDto> updatedList = new ArrayList<>();

        //삭제된 ArtistNote DB에서 삭제
        List<ArtistNote> notesInDB = artistNoteRepository.findByArtistId(artistService.getArtistByUserId(user.getId()).getId());
        Set<Long> requestIds = notes.stream()
                .map(ArtistNoteDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<ArtistNote> toDelete = notesInDB.stream()
                .filter(note -> !requestIds.contains(note.getId()))
                .collect(Collectors.toList());

        if (!toDelete.isEmpty()) {
            artistNoteRepository.deleteAll(toDelete);
        }
        
        //새로 추가된 ArtistNote DB에 추가
        for (ArtistNoteDto dto : notes) {
            ArtistNote note = artistNoteRepository.findById(dto.getId())
                    .orElseThrow(() -> new MapperException(ErrorCode.POST_NOT_FOUND));

            if (!note.getArtist().getUser().getId().equals(user.getId())) {
                throw new MapperException(ErrorCode.ACCESS_DENIED);
            }

            boolean isUpdated = false;
            if (!Objects.equals(note.getArtistNoteType(), dto.getArtistNoteType())) {
                note.setArtistNoteType(dto.getArtistNoteType());
                isUpdated = true;
            }
            if (!Objects.equals(note.getYear(), dto.getYear())) {
                note.setYear(dto.getYear());
                isUpdated = true;
            }
            if (!Objects.equals(note.getDescription(), dto.getDescription())) {
                note.setDescription(dto.getDescription());
                isUpdated = true;
            }

            if (isUpdated) {
                artistNoteRepository.save(note);
            }
            updatedList.add(ArtistNoteDto.from(note));
        }

        return updatedList;
    }
}
