package ATORY.atory.domain.artist.repository;

import ATORY.atory.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    boolean existsByUser_Id(Long userId);

    @Query("select a.id from Artist a where a.user.id = :userId")
    Optional<Long> findIdByUserId(Long userId);
}