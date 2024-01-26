package tech.noetzold.healthcheckAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.noetzold.healthcheckAPI.model.MetricResponse;

@Repository
public interface MetricsRepository extends JpaRepository<MetricResponse, Long> {
}
