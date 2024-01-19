package tech.noetzold.healthcheckAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.healthcheckAPI.model.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
}
