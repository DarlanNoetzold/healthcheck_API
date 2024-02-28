package tech.noetzold.healthcheckgate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.healthcheckgate.model.ModelAccuracy;

import java.util.Optional;

public interface ModelAccuracyRepository extends JpaRepository<ModelAccuracy, Long> {
    Optional<ModelAccuracy> findByModelNameAndMetricName(String modelName, String metricName);

}
