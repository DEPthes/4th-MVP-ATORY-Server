package ATORY.atory.domain.artist.service;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
import ATORY.atory.domain.artist.dto.ArtistDto;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.user.service.UserService;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final UserService userService;

    public ArtistDto findById(Long id) {
        Artist found = artistRepository.findById(id)
                .orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

        return ArtistDto.builder()
                .id(found.getId())
                .user(userService.findById(found.getId()))
                .artistNotes(ArtistNoteDto.from(found.getArtistNotes()))
                .birth(found.getBirth())
                .educationBackground(found.getEducationBackground())
                .disclosureStatus(found.getDisclosureStatus())
                .build();
    }
}
