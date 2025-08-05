package ATORY.atory.domain.collector.repository;

import ATORY.atory.domain.collector.entity.Collector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollectorRepository extends JpaRepository<Collector, Long> {
    Optional<Collector> findByUserId(Long userId);
}
