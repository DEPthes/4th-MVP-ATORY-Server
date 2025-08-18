package ATORY.atory.domain.gallery.repository;

import ATORY.atory.domain.gallery.entity.Gallery;
import ATORY.atory.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    Gallery findByUser(User user);
}
