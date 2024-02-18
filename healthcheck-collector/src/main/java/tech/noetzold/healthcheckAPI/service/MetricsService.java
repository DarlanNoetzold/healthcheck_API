package tech.noetzold.healthcheckAPI.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.noetzold.healthcheckAPI.client.MetricsClient;
import tech.noetzold.healthcheckAPI.model.Measurement;
import tech.noetzold.healthcheckAPI.model.MetricResponse;
import tech.noetzold.healthcheckAPI.model.MetricResponseGroupedDTO;
import tech.noetzold.healthcheckAPI.model.Tag;
import tech.noetzold.healthcheckAPI.repository.MeasurementRepository;
import tech.noetzold.healthcheckAPI.repository.MetricsRepository;
import tech.noetzold.healthcheckAPI.repository.TagRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MetricsService {

    @Autowired
    private MetricsClient metricsClient;

    @Autowired
    private MetricsRepository metricsRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private TagRepository tagRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(MetricsService.class);

    @Transactional
    public void updateCalculatedFields() {
        entityManager.createNativeQuery("CALL update_measurement_calculated_field()").executeUpdate();
    }

    private void calculateAndSetFlagForMeasurement(Measurement measurement) {
        List<Measurement> existingMeasurements = measurementRepository.findByMetricResponse(measurement.getMetricResponse());

        if (!existingMeasurements.isEmpty()) {
            double sum = existingMeasurements.stream().mapToDouble(Measurement::getValue).sum();
            double mean = sum / existingMeasurements.size();
            double variance = existingMeasurements.stream()
                    .mapToDouble(m -> Math.pow(m.getValue() - mean, 2))
                    .sum() / existingMeasurements.size();
            double stdDev = Math.sqrt(variance);

            boolean isHigher = measurement.getValue() > (mean + stdDev);
            measurement.setAlert(isHigher);
        } else {
            measurement.setAlert(false);
        }
    }

    public List<MetricResponse> getAllMetricsPaginated(Pageable pageable) {
        logger.info("Iniciando getAllMetricsPaginated");
        List<MetricResponse> responses = metricsRepository.findAll(pageable).getContent();
        logger.info("Finalizando getAllMetricsPaginated");
        return responses;
    }

    public List<MetricResponseGroupedDTO> getAllMetricsGroupedByNamePaginated(Pageable pageable) {
        logger.info("Iniciando getAllMetricsGroupedByNamePaginated");
        Page<MetricResponse> metricsPage = metricsRepository.findAll(pageable);
        List<MetricResponse> metrics = metricsPage.getContent();

        Map<String, List<MetricResponse>> groupedMetrics = metrics.stream()
                .collect(Collectors.groupingBy(MetricResponse::getName));

        List<MetricResponseGroupedDTO> result = groupedMetrics.entrySet().stream()
                .map(entry -> new MetricResponseGroupedDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        logger.info("Finalizando getAllMetricsGroupedByNamePaginated");
        return result;
    }

    @Transactional
    public void fetchAndSaveApplicationReadyTime() {
        MetricResponse response = metricsClient.getApplicationReadyTime();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveApplicationStartedTime() {
        MetricResponse response = metricsClient.getApplicationStartedTime();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveCacheGets() {
        MetricResponse response = metricsClient.getCacheGets();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveCacheLockDuration() {
        MetricResponse response = metricsClient.getCacheLockDuration();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveCachePuts() {
        MetricResponse response = metricsClient.getCachePuts();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveCacheRemovals() {
        MetricResponse response = metricsClient.getCacheRemovals();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveDiskFree() {
        MetricResponse response = metricsClient.getDiskFree();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveDiskTotal() {
        MetricResponse response = metricsClient.getDiskTotal();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveExecutorActive() {
        MetricResponse response = metricsClient.getExecutorActive();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveExecutorCompleted() {
        MetricResponse response = metricsClient.getExecutorCompleted();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveExecutorPoolCore() {
        MetricResponse response = metricsClient.getExecutorPoolCore();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveExecutorPoolMax() {
        MetricResponse response = metricsClient.getExecutorPoolMax();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveExecutorPoolSize() {
        MetricResponse response = metricsClient.getExecutorPoolSize();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveExecutorQueueRemaining() {
        MetricResponse response = metricsClient.getExecutorQueueRemaining();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveExecutorQueued() {
        MetricResponse response = metricsClient.getExecutorQueued();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHikaricpConnections() {
        MetricResponse response = metricsClient.getHikaricpConnections();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHikaricpConnectionsAcquire() {
        MetricResponse response = metricsClient.getHikaricpConnectionsAcquire();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHikaricpConnectionsActive() {
        MetricResponse response = metricsClient.getHikaricpConnectionsActive();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHikaricpConnectionsCreation() {
        MetricResponse response = metricsClient.getHikaricpConnectionsCreation();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHikaricpConnectionsIdle() {
        MetricResponse response = metricsClient.getHikaricpConnectionsIdle();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHikaricpConnectionsMax() {
        MetricResponse response = metricsClient.getHikaricpConnectionsMax();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHikaricpConnectionsMin() {
        MetricResponse response = metricsClient.getHikaricpConnectionsMin();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHikaricpConnectionsPending() {
        MetricResponse response = metricsClient.getHikaricpConnectionsPending();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHikaricpConnectionsTimeout() {
        MetricResponse response = metricsClient.getHikaricpConnectionsTimeout();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHikaricpConnectionsUsage() {
        MetricResponse response = metricsClient.getHikaricpConnectionsUsage();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHttpServerRequests() {
        MetricResponse response = metricsClient.getHttpServerRequests();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveHttpServerRequestsActive() {
        MetricResponse response = metricsClient.getHttpServerRequestsActive();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJdbcConnectionsActive() {
        MetricResponse response = metricsClient.getJdbcConnectionsActive();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJdbcConnectionsIdle() {
        MetricResponse response = metricsClient.getJdbcConnectionsIdle();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJdbcConnectionsMax() {
        MetricResponse response = metricsClient.getJdbcConnectionsMax();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJdbcConnectionsMin() {
        MetricResponse response = metricsClient.getJdbcConnectionsMin();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmBufferCount() {
        MetricResponse response = metricsClient.getJvmBufferCount();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmBufferMemoryUsed() {
        MetricResponse response = metricsClient.getJvmBufferMemoryUsed();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmBufferTotalCapacity() {
        MetricResponse response = metricsClient.getJvmBufferTotalCapacity();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmClassesLoaded() {
        MetricResponse response = metricsClient.getJvmClassesLoaded();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmClassesUnloaded() {
        MetricResponse response = metricsClient.getJvmClassesUnloaded();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmCompilationTime() {
        MetricResponse response = metricsClient.getJvmCompilationTime();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmGcLiveDataSize() {
        MetricResponse response = metricsClient.getJvmGcLiveDataSize();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmGcMaxDataSize() {
        MetricResponse response = metricsClient.getJvmGcMaxDataSize();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmGcMemoryAllocated() {
        MetricResponse response = metricsClient.getJvmGcMemoryAllocated();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmGcMemoryPromoted() {
        MetricResponse response = metricsClient.getJvmGcMemoryPromoted();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmGcOverhead() {
        MetricResponse response = metricsClient.getJvmGcOverhead();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmGcPause() {
        MetricResponse response = metricsClient.getJvmGcPause();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmInfo() {
        MetricResponse response = metricsClient.getJvmInfo();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmMemoryCommitted() {
        MetricResponse response = metricsClient.getJvmMemoryCommitted();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmMemoryMax() {
        MetricResponse response = metricsClient.getJvmMemoryMax();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmMemoryUsageAfterGc() {
        MetricResponse response = metricsClient.getJvmMemoryUsageAfterGc();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmMemoryUsed() {
        MetricResponse response = metricsClient.getJvmMemoryUsed();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmThreadsDaemon() {
        MetricResponse response = metricsClient.getJvmThreadsDaemon();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmThreadsLive() {
        MetricResponse response = metricsClient.getJvmThreadsLive();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmThreadsPeak() {
        MetricResponse response = metricsClient.getJvmThreadsPeak();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmThreadsStarted() {
        MetricResponse response = metricsClient.getJvmThreadsStarted();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveJvmThreadsStates() {
        MetricResponse response = metricsClient.getJvmThreadsStates();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveLettuceCommandCompletion() {
        MetricResponse response = metricsClient.getLettuceCommandCompletion();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveLettuceCommandFirstresponse() {
        MetricResponse response = metricsClient.getLettuceCommandFirstresponse();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveLogbackEvents() {
        MetricResponse response = metricsClient.getLogbackEvents();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveProcessCpuUsage() {
        MetricResponse response = metricsClient.getProcessCpuUsage();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveProcessFilesMax() {
        MetricResponse response = metricsClient.getProcessFilesMax();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveProcessFilesOpen() {
        MetricResponse response = metricsClient.getProcessFilesOpen();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveProcessStartTime() {
        MetricResponse response = metricsClient.getProcessStartTime();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveProcessUptime() {
        MetricResponse response = metricsClient.getProcessUptime();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveSpringDataRepositoryInvocations() {
        MetricResponse response = metricsClient.getSpringDataRepositoryInvocations();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveSystemCpuCount() {
        MetricResponse response = metricsClient.getSystemCpuCount();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveSystemCpuUsage() {
        MetricResponse response = metricsClient.getSystemCpuUsage();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }
    @Transactional
    public void fetchAndSaveSystemLoadAverage1M() {
        MetricResponse response = metricsClient.getSystemLoadAverage1M();
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {
                calculateAndSetFlagForMeasurement(measurement);
                measurement.setMetricResponse(savedResponse);
                measurementRepository.save(measurement);
            }
        }

        if (savedResponse.getAvailableTags() != null) {
            for (Tag tag : savedResponse.getAvailableTags()) {
                tag.setMetricResponse(savedResponse);
                tagRepository.save(tag);
            }
        }
    }

}

