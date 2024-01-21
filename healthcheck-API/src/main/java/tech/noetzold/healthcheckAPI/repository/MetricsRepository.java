package tech.noetzold.healthcheckAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.noetzold.healthcheckAPI.model.MetricResponse;
import tech.noetzold.healthcheckAPI.model.MetricResponseGroupedDTO;

import java.util.List;

public interface MetricsRepository extends JpaRepository<MetricResponse, Long> {
    @Query("SELECT mr.name as name, GROUP_CONCAT(mr) as metricResponses " +
            "FROM MetricResponse mr " +
            "GROUP BY mr.name")
    List<MetricResponseGroupedDTO> findAllGroupedByName();
}
