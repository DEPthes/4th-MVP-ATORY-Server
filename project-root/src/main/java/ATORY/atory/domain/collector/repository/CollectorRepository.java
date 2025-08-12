package ATORY.atory.domain.collector.repository;

import ATORY.atory.domain.collector.entity.Collector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectorRepository extends JpaRepository<Collector, Long> {
    Optional<Collector> findByUserId(Long userId);
}