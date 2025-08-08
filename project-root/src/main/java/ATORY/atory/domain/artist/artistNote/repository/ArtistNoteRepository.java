package ATORY.atory.domain.artist.artistNote.repository;

import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistNoteRepository extends JpaRepository<ArtistNote, Long> {
    List<ArtistNote> findByArtistId(Long artistId);

}
