package tech.noetzold.healthcheckgate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.healthcheckgate.model.Metric;
import tech.noetzold.healthcheckgate.repository.MetricRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/healthcheck/v1/gate/metrics")
@Cacheable("metric")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
public class MetricController {

    @Autowired
    private MetricRepository metricRepository;

    @GetMapping
    public List<Metric> getAllMetrics() {
        return metricRepository.findAll();
    }

    @PostMapping
    public Metric createMetric(@RequestBody Metric metric) {
        Optional<Metric> existingMetric = metricRepository.findByName(metric.getName());

        return existingMetric.orElseGet(() -> metricRepository.save(metric));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Metric> getMetricById(@PathVariable Long id) {
        return metricRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Metric> updateMetric(@PathVariable Long id, @RequestBody Metric metricDetails) {
        return metricRepository.findById(id)
                .map(metric -> {
                    metric.setName(metricDetails.getName());
                    metric.setValueType(metricDetails.getValueType());
                    return ResponseEntity.ok(metricRepository.save(metric));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetric(@PathVariable Long id) {
        metricRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}