package ATORY.atory.domain.artist.artistNote.service;

import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import ATORY.atory.domain.artist.artistNote.repository.ArtistNoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistNoteService {

    private final ArtistNoteRepository artistNoteRepository;

    public List<ArtistNote> getByArtistId(Long artistId){
        return artistNoteRepository.findByArtistId(artistId);
    }
}
