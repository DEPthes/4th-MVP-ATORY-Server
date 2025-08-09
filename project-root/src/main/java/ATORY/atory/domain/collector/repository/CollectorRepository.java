package ATORY.atory.domain.collector.repository;

import ATORY.atory.domain.collector.entity.Collector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectorRepository extends JpaRepository<Collector, Long> {
    boolean existsByUser_Id(Long userId);
}