package ATORY.atory.domain.artist.repository;

import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findByUser(User user);
}
