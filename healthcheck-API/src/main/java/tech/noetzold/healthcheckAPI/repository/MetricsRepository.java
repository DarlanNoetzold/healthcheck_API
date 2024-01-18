package tech.noetzold.healthcheckAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.healthcheckAPI.model.MetricResponse;

public interface MetricsRepository extends JpaRepository<MetricResponse, Long> {
}
