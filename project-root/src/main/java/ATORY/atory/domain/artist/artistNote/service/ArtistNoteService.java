package ATORY.atory.domain.artist.artistNote.service;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDetailResponseDto;
import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteListItemDto;
import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import ATORY.atory.domain.artist.artistNote.mapper.ArtistNoteMapper;
import ATORY.atory.domain.artist.artistNote.repository.ArtistNoteRepository;
import ATORY.atory.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistNoteService {

    private final ArtistNoteRepository artistNoteRepository;

    public Page<ArtistNoteListItemDto> getNotes(Pageable pageable) {
        Page<ArtistNote> page = artistNoteRepository.findAllByOrderByIdDesc(pageable);
        return page.map(ArtistNoteMapper::toListItem);
    }

    public ArtistNoteDetailResponseDto getNote(Long noteId) {
        ArtistNote note = artistNoteRepository.findByIdWithArtistAndUser(noteId)
                .orElseThrow(() -> new NotFoundException("ArtistNote not found: " + noteId));
        return ArtistNoteMapper.toDetail(note);
    }
}
