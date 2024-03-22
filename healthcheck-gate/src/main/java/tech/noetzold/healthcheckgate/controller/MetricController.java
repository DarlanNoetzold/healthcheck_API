package tech.noetzold.healthcheckgate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.healthcheckgate.model.Metric;
import tech.noetzold.healthcheckgate.repository.MetricRepository;
import tech.noetzold.healthcheckgate.service.ModelAccuracyService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/healthcheck/v1/gate/metrics")
@Cacheable("metric")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('ADMIN')")
public class MetricController {

    @Autowired
    private MetricRepository metricRepository;

    private static final Logger logger = LoggerFactory.getLogger(MetricController.class);

    @GetMapping
    public List<Metric> getAllMetrics() {
        logger.info("Fetching all metrics");
        return metricRepository.findAll();
    }

    @PostMapping
    public Metric createMetric(@RequestBody Metric metric) {
        logger.info("Attempting to create metric: {}", metric);
        Optional<Metric> existingMetric = metricRepository.findByName(metric.getName());
        Metric savedMetric = existingMetric.orElseGet(() -> {
            Metric newMetric = metricRepository.save(metric);
            logger.info("New metric created successfully: {}", newMetric);
            return newMetric;
        });
        if (existingMetric.isPresent()) {
            logger.warn("Metric already exists and was not created: {}", metric);
        }
        return savedMetric;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Metric> getMetricById(@PathVariable Long id) {
        logger.info("Fetching metric by ID: {}", id);
        return metricRepository.findById(id)
                .map(metric -> {
                    logger.info("Metric found: {}", metric);
                    return ResponseEntity.ok(metric);
                })
                .orElseGet(() -> {
                    logger.warn("Metric not found for ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Metric> updateMetric(@PathVariable Long id, @RequestBody Metric metricDetails) {
        logger.info("Attempting to update metric ID: {} with details: {}", id, metricDetails);
        return metricRepository.findById(id)
                .map(metric -> {
                    metric.setName(metricDetails.getName());
                    metric.setValueType(metricDetails.getValueType());
                    Metric updatedMetric = metricRepository.save(metric);
                    logger.info("Metric updated successfully: {}", updatedMetric);
                    return ResponseEntity.ok(updatedMetric);
                })
                .orElseGet(() -> {
                    logger.warn("Metric not found for update ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetric(@PathVariable Long id) {
        logger.info("Attempting to delete metric ID: {}", id);
        metricRepository.deleteById(id);
        logger.info("Metric deleted successfully ID: {}", id);
        return ResponseEntity.ok().build();
    }
}