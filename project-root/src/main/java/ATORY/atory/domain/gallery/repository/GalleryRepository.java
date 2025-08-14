package ATORY.atory.domain.gallery.repository;

import ATORY.atory.domain.gallery.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    boolean existsByUser_Id(Long id);
    @Query("SELECT g.location FROM Gallery g WHERE g.user.id = :userId")
    String findLocationByUserId(@Param("userId") Long userId);
}
