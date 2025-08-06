package ATORY.atory.domain.artist.repository;

import ATORY.atory.domain.artist.entity.YouthArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YouthArtistRepository extends JpaRepository<YouthArtist, Long> {
    YouthArtist findByUserId(Long userId);
}
