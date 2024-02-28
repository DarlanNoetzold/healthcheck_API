package tech.noetzold.healthcheckgate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.healthcheckgate.model.Metric;

import java.util.Optional;

public interface MetricRepository extends JpaRepository<Metric, Long> {

    Optional<Metric> findByName(String name);
}
