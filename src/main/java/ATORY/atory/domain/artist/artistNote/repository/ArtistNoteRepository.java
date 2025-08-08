package ATORY.atory.domain.artist.artistNote.repository;

import ATORY.atory.domain.artist.entity.YouthArtist;
import ATORY.atory.domain.post.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistNoteRepository extends JpaRepository<YouthArtist, Long> {
    
    // 작가노트 게시물이 있는 작가들 조회 (페이징)
    @Query("SELECT DISTINCT a FROM YouthArtist a " +
           "JOIN a.user u " +
           "WHERE u.isProfileCompleted = true " +
           "AND EXISTS (SELECT 1 FROM Post p WHERE p.user = u AND p.postType = :postType) " +
           "ORDER BY a.id DESC")
    Page<YouthArtist> findArtistsWithNotes(Pageable pageable, @Param("postType") PostType postType);
    
    // 특정 작가의 작가노트 게시물 수 조회
    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId AND p.postType = :postType")
    long countArtistNotesByUserId(@Param("userId") Long userId, @Param("postType") PostType postType);
}
