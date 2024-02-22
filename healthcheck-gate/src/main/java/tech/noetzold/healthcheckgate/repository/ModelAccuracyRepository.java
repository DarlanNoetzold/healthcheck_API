package tech.noetzold.healthcheckgate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.healthcheckgate.model.ModelAccuracy;

public interface ModelAccuracyRepository extends JpaRepository<ModelAccuracy, Long> {
}
