package ATORY.atory.domain.artist.artistNote.repository;

import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ArtistNoteRepository extends JpaRepository<ArtistNote, Long> {

    Page<ArtistNote> findAllByOrderByIdDesc(Pageable pageable);

    @Query("""
        select n from ArtistNote n
        join fetch n.artist a
        join fetch a.user u
        where n.id = :id
    """)
    Optional<ArtistNote> findByIdWithArtistAndUser(@Param("id") Long id);

    @EntityGraph(attributePaths = {"artist", "artist.user"})
    Optional<ArtistNote> findById(Long id);
}