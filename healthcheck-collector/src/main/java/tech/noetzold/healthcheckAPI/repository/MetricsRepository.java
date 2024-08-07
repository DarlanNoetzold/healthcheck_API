package tech.noetzold.healthcheckAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.healthcheckAPI.model.MetricResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MetricsRepository extends JpaRepository<MetricResponse, Long> {
    Page<MetricResponse> findAll(Pageable pageable);

    List<MetricResponse> findByName(String metricResponseName);
}

