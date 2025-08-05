package ATORY.atory.domain.artist.artistNote.repository;

import ATORY.atory.domain.artist.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistNoteRepository extends JpaRepository<Artist, Long> {
    
    // 작가노트 게시물이 있는 작가들 조회 (페이징)
    @Query("SELECT DISTINCT a FROM Artist a " +
           "JOIN a.user u " +
           "WHERE u.isProfileCompleted = true " +
           "AND EXISTS (SELECT 1 FROM Post p WHERE p.user = u AND p.type = 'ARTIST_NOTE') " +
           "ORDER BY a.id DESC")
    Page<Artist> findArtistsWithNotes(Pageable pageable);
    
    // 특정 작가의 작가노트 게시물 수 조회
    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId AND p.type = 'ARTIST_NOTE'")
    long countArtistNotesByUserId(@Param("userId") Long userId);
}
