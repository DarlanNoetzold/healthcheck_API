package tech.noetzold.healthcheckAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.noetzold.healthcheckAPI.model.Measurement;
import tech.noetzold.healthcheckAPI.model.MetricResponse;

import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByMetricResponse(MetricResponse metricResponse);
}
