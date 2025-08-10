package ATORY.atory.domain.gallery.repository;

import ATORY.atory.domain.gallery.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
}
