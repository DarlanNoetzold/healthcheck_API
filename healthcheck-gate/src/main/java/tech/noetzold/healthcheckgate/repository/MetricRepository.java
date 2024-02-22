package tech.noetzold.healthcheckgate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.healthcheckgate.model.Metric;

public interface MetricRepository extends JpaRepository<Metric, Long> {
}
