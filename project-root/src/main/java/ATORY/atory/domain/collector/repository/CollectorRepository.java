package ATORY.atory.domain.collector.repository;

import ATORY.atory.domain.collector.entity.Collector;
import ATORY.atory.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectorRepository extends JpaRepository<Collector, Long> {
    Collector findByUser(User user);
}
