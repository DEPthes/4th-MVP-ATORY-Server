package ATORY.atory.domain.collector.repository;

import ATORY.atory.domain.collector.entity.Collector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectorRepository extends JpaRepository<Collector, Long> {
    boolean existsByUser_Id(Long id);
    @Query("SELECT ac.birth FROM Collector ac WHERE ac.user.id = :userId")
    String findBirthByUserId(@Param("userId") Long userId);

    @Query("SELECT ac.educationBackground FROM Collector ac WHERE ac.user.id = :userId")
    String findEducationBackgroundByUserId(@Param("userId") Long userId);

    @Query("SELECT ac.disclosureStatus FROM Collector ac WHERE ac.user.id = :userId")
    Boolean findDisclosureStatusByUserId(@Param("userId") Long userId);
}
