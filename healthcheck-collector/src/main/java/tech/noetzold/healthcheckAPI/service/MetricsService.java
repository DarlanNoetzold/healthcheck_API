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
        entityManager.createNativeQuery("CALL update_is_alert_flag()").executeUpdate();
    }

    private boolean calculateAndSetFlagForMeasurement(Measurement measurement) {
        String metricResponseName = measurement.getMetricResponse().getName();
        List<MetricResponse> metricResponses = metricsRepository.findByName(metricResponseName);

        if (!metricResponses.isEmpty()) {
            List<Measurement> allMeasurements = metricResponses.stream()
                    .flatMap(mr -> mr.getMeasurements().stream())
                    .toList();

            double mean = allMeasurements.stream().mapToDouble(Measurement::getValue).average().orElse(Double.NaN);
            double stdDev = Math.sqrt(allMeasurements.stream()
                    .mapToDouble(m -> Math.pow(m.getValue() - mean, 2))
                    .average().orElse(Double.NaN));

            boolean isHigher = measurement.getValue() > (mean + stdDev);
            logger.info("Is higher: {} - mean: {} - stdDev {}", isHigher, mean, stdDev);
            return isHigher;
        } else {
            return false;
        }
    }

    public MetricResponse enrichMetricResponseWithJvmDetails(MetricResponse metricResponse) {
        MetricResponse jvmDetails = metricsClient.getJvmDetails();
        metricResponse.setJvmArguments(jvmDetails.getJvmArguments());
        metricResponse.setHeapMemoryMax(jvmDetails.getHeapMemoryMax());
        metricResponse.setHeapMemoryUsed(jvmDetails.getHeapMemoryUsed());
        metricResponse.setGarbageCollectors(jvmDetails.getGarbageCollectors());
        return metricResponse;
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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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

    //@Transactional
    //public void fetchAndSaveProcessFilesMax() {
    //    MetricResponse response = metricsClient.getProcessFilesMax();
    //    response = enrichMetricResponseWithJvmDetails(response);
    //    MetricResponse savedResponse = metricsRepository.save(response);
//
    //    if (savedResponse.getMeasurements() != null) {
    //        for (Measurement measurement : savedResponse.getMeasurements()) {
//
    //            measurement.setMetricResponse(savedResponse);
    //            measurementRepository.save(measurement);
    //        }
    //    }
//
    //    if (savedResponse.getAvailableTags() != null) {
    //        for (Tag tag : savedResponse.getAvailableTags()) {
    //            tag.setMetricResponse(savedResponse);
    //            tagRepository.save(tag);
    //        }
    //    }
    //}
    @Transactional
    public void fetchAndSaveProcessFilesOpen() {
        MetricResponse response = metricsClient.getProcessFilesOpen();
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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
        response = enrichMetricResponseWithJvmDetails(response);
        MetricResponse savedResponse = metricsRepository.save(response);

        if (savedResponse.getMeasurements() != null) {
            for (Measurement measurement : savedResponse.getMeasurements()) {

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

