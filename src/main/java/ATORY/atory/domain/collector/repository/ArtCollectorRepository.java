package ATORY.atory.domain.collector.repository;

import ATORY.atory.domain.collector.entity.ArtCollector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtCollectorRepository extends JpaRepository<ArtCollector, Long> {
    ArtCollector findByUserId(Long userId);
}
