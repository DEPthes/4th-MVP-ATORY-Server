package ATORY.atory.domain.artist.repository;

import ATORY.atory.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    boolean existsByUser_Id(Long id);

    @Query("SELECT a.birth FROM Artist a WHERE a.user.id = :userId")
    String findBirthByUserId(@Param("userId") Long userId);

    @Query("SELECT a.educationBackground FROM Artist a WHERE a.user.id = :userId")
    String findEducationBackgroundByUserId(@Param("userId") Long userId);

    @Query("SELECT a.disclosureStatus FROM Artist a WHERE a.user.id = :userId")
    Boolean findDisclosureStatusByUserId(@Param("userId") Long userId);

}
