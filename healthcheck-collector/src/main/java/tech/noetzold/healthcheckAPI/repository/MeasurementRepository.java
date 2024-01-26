package tech.noetzold.healthcheckAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.noetzold.healthcheckAPI.model.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
}
