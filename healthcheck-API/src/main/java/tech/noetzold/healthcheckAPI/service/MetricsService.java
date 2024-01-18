package tech.noetzold.healthcheckAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.noetzold.healthcheckAPI.client.MetricsClient;
import tech.noetzold.healthcheckAPI.model.MetricResponse;
import tech.noetzold.healthcheckAPI.repository.MetricsRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    @Autowired
    private MetricsClient metricsClient;

    @Autowired
    private MetricsRepository metricsRepository;


    public void fetchAndSaveApplicationReadyTime() {
        MetricResponse response = metricsClient.getApplicationReadyTime();
        metricsRepository.save(response);
    }

    public void fetchAndSaveApplicationStartedTime() {
        MetricResponse response = metricsClient.getApplicationStartedTime();
        metricsRepository.save(response);
    }

    public void fetchAndSaveCacheGets() {
        MetricResponse response = metricsClient.getCacheGets();
        metricsRepository.save(response);
    }

    public void fetchAndSaveCacheLockDuration() {
        MetricResponse response = metricsClient.getCacheLockDuration();
        metricsRepository.save(response);
    }

    public void fetchAndSaveCachePuts() {
        MetricResponse response = metricsClient.getCachePuts();
        metricsRepository.save(response);
    }

    public void fetchAndSaveCacheRemovals() {
        MetricResponse response = metricsClient.getCacheRemovals();
        metricsRepository.save(response);
    }

    public void fetchAndSaveDiskFree() {
        MetricResponse response = metricsClient.getDiskFree();
        metricsRepository.save(response);
    }

    public void fetchAndSaveDiskTotal() {
        MetricResponse response = metricsClient.getDiskTotal();
        metricsRepository.save(response);
    }

    public void fetchAndSaveExecutorActive() {
        MetricResponse response = metricsClient.getExecutorActive();
        metricsRepository.save(response);
    }

    public void fetchAndSaveExecutorCompleted() {
        MetricResponse response = metricsClient.getExecutorCompleted();
        metricsRepository.save(response);
    }

    public void fetchAndSaveExecutorPoolCore() {
        MetricResponse response = metricsClient.getExecutorPoolCore();
        metricsRepository.save(response);
    }

    public void fetchAndSaveExecutorPoolMax() {
        MetricResponse response = metricsClient.getExecutorPoolMax();
        metricsRepository.save(response);
    }

    public void fetchAndSaveExecutorPoolSize() {
        MetricResponse response = metricsClient.getExecutorPoolSize();
        metricsRepository.save(response);
    }

    public void fetchAndSaveExecutorQueueRemaining() {
        MetricResponse response = metricsClient.getExecutorQueueRemaining();
        metricsRepository.save(response);
    }

    public void fetchAndSaveExecutorQueued() {
        MetricResponse response = metricsClient.getExecutorQueued();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHikaricpConnections() {
        MetricResponse response = metricsClient.getHikaricpConnections();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHikaricpConnectionsAcquire() {
        MetricResponse response = metricsClient.getHikaricpConnectionsAcquire();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHikaricpConnectionsActive() {
        MetricResponse response = metricsClient.getHikaricpConnectionsActive();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHikaricpConnectionsCreation() {
        MetricResponse response = metricsClient.getHikaricpConnectionsCreation();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHikaricpConnectionsIdle() {
        MetricResponse response = metricsClient.getHikaricpConnectionsIdle();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHikaricpConnectionsMax() {
        MetricResponse response = metricsClient.getHikaricpConnectionsMax();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHikaricpConnectionsMin() {
        MetricResponse response = metricsClient.getHikaricpConnectionsMin();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHikaricpConnectionsPending() {
        MetricResponse response = metricsClient.getHikaricpConnectionsPending();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHikaricpConnectionsTimeout() {
        MetricResponse response = metricsClient.getHikaricpConnectionsTimeout();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHikaricpConnectionsUsage() {
        MetricResponse response = metricsClient.getHikaricpConnectionsUsage();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHttpServerRequests() {
        MetricResponse response = metricsClient.getHttpServerRequests();
        metricsRepository.save(response);
    }

    public void fetchAndSaveHttpServerRequestsActive() {
        MetricResponse response = metricsClient.getHttpServerRequestsActive();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJdbcConnectionsActive() {
        MetricResponse response = metricsClient.getJdbcConnectionsActive();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJdbcConnectionsIdle() {
        MetricResponse response = metricsClient.getJdbcConnectionsIdle();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJdbcConnectionsMax() {
        MetricResponse response = metricsClient.getJdbcConnectionsMax();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJdbcConnectionsMin() {
        MetricResponse response = metricsClient.getJdbcConnectionsMin();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmBufferCount() {
        MetricResponse response = metricsClient.getJvmBufferCount();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmBufferMemoryUsed() {
        MetricResponse response = metricsClient.getJvmBufferMemoryUsed();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmBufferTotalCapacity() {
        MetricResponse response = metricsClient.getJvmBufferTotalCapacity();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmClassesLoaded() {
        MetricResponse response = metricsClient.getJvmClassesLoaded();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmClassesUnloaded() {
        MetricResponse response = metricsClient.getJvmClassesUnloaded();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmCompilationTime() {
        MetricResponse response = metricsClient.getJvmCompilationTime();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmGcLiveDataSize() {
        MetricResponse response = metricsClient.getJvmGcLiveDataSize();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmGcMaxDataSize() {
        MetricResponse response = metricsClient.getJvmGcMaxDataSize();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmGcMemoryAllocated() {
        MetricResponse response = metricsClient.getJvmGcMemoryAllocated();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmGcMemoryPromoted() {
        MetricResponse response = metricsClient.getJvmGcMemoryPromoted();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmGcOverhead() {
        MetricResponse response = metricsClient.getJvmGcOverhead();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmGcPause() {
        MetricResponse response = metricsClient.getJvmGcPause();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmInfo() {
        MetricResponse response = metricsClient.getJvmInfo();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmMemoryCommitted() {
        MetricResponse response = metricsClient.getJvmMemoryCommitted();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmMemoryMax() {
        MetricResponse response = metricsClient.getJvmMemoryMax();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmMemoryUsageAfterGc() {
        MetricResponse response = metricsClient.getJvmMemoryUsageAfterGc();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmMemoryUsed() {
        MetricResponse response = metricsClient.getJvmMemoryUsed();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmThreadsDaemon() {
        MetricResponse response = metricsClient.getJvmThreadsDaemon();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmThreadsLive() {
        MetricResponse response = metricsClient.getJvmThreadsLive();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmThreadsPeak() {
        MetricResponse response = metricsClient.getJvmThreadsPeak();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmThreadsStarted() {
        MetricResponse response = metricsClient.getJvmThreadsStarted();
        metricsRepository.save(response);
    }

    public void fetchAndSaveJvmThreadsStates() {
        MetricResponse response = metricsClient.getJvmThreadsStates();
        metricsRepository.save(response);
    }

    public void fetchAndSaveLettuceCommandCompletion() {
        MetricResponse response = metricsClient.getLettuceCommandCompletion();
        metricsRepository.save(response);
    }

    public void fetchAndSaveLettuceCommandFirstresponse() {
        MetricResponse response = metricsClient.getLettuceCommandFirstresponse();
        metricsRepository.save(response);
    }

    public void fetchAndSaveLogbackEvents() {
        MetricResponse response = metricsClient.getLogbackEvents();
        metricsRepository.save(response);
    }

    public void fetchAndSaveProcessCpuUsage() {
        MetricResponse response = metricsClient.getProcessCpuUsage();
        metricsRepository.save(response);
    }

    public void fetchAndSaveProcessFilesMax() {
        MetricResponse response = metricsClient.getProcessFilesMax();
        metricsRepository.save(response);
    }

    public void fetchAndSaveProcessFilesOpen() {
        MetricResponse response = metricsClient.getProcessFilesOpen();
        metricsRepository.save(response);
    }

    public void fetchAndSaveProcessStartTime() {
        MetricResponse response = metricsClient.getProcessStartTime();
        metricsRepository.save(response);
    }

    public void fetchAndSaveProcessUptime() {
        MetricResponse response = metricsClient.getProcessUptime();
        metricsRepository.save(response);
    }

    public void fetchAndSaveSpringDataRepositoryInvocations() {
        MetricResponse response = metricsClient.getSpringDataRepositoryInvocations();
        metricsRepository.save(response);
    }

    public void fetchAndSaveSystemCpuCount() {
        MetricResponse response = metricsClient.getSystemCpuCount();
        metricsRepository.save(response);
    }

    public void fetchAndSaveSystemCpuUsage() {
        MetricResponse response = metricsClient.getSystemCpuUsage();
        metricsRepository.save(response);
    }

    public void fetchAndSaveSystemLoadAverage1M() {
        MetricResponse response = metricsClient.getSystemLoadAverage1M();
        metricsRepository.save(response);
    }

}

