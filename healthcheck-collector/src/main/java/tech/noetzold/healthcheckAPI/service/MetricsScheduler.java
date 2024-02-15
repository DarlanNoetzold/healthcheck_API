package tech.noetzold.healthcheckAPI.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetricsScheduler {

    @Autowired
    private MetricsService metricsService;

    @Scheduled(fixedRate = 300000) // 15 minutos em milissegundos
    public void fetchAndSaveMetrics() {
        metricsService.fetchAndSaveApplicationReadyTime();
        metricsService.fetchAndSaveApplicationStartedTime();
        metricsService.fetchAndSaveCacheGets();
        metricsService.fetchAndSaveCacheLockDuration();
        metricsService.fetchAndSaveCachePuts();
        metricsService.fetchAndSaveCacheRemovals();
        metricsService.fetchAndSaveDiskFree();
        metricsService.fetchAndSaveDiskTotal();
        metricsService.fetchAndSaveExecutorActive();
        metricsService.fetchAndSaveExecutorCompleted();
        metricsService.fetchAndSaveExecutorPoolCore();
        metricsService.fetchAndSaveExecutorPoolMax();
        metricsService.fetchAndSaveExecutorPoolSize();
        metricsService.fetchAndSaveExecutorQueueRemaining();
        metricsService.fetchAndSaveExecutorQueued();
        metricsService.fetchAndSaveHikaricpConnections();
        metricsService.fetchAndSaveHikaricpConnectionsAcquire();
        metricsService.fetchAndSaveHikaricpConnectionsActive();
        metricsService.fetchAndSaveHikaricpConnectionsCreation();
        metricsService.fetchAndSaveHikaricpConnectionsIdle();
        metricsService.fetchAndSaveHikaricpConnectionsMax();
        metricsService.fetchAndSaveHikaricpConnectionsMin();
        metricsService.fetchAndSaveHikaricpConnectionsPending();
        metricsService.fetchAndSaveHikaricpConnectionsTimeout();
        metricsService.fetchAndSaveHikaricpConnectionsUsage();
        metricsService.fetchAndSaveHttpServerRequests();
        metricsService.fetchAndSaveHttpServerRequestsActive();
        metricsService.fetchAndSaveJdbcConnectionsActive();
        metricsService.fetchAndSaveJdbcConnectionsIdle();
        metricsService.fetchAndSaveJdbcConnectionsMax();
        metricsService.fetchAndSaveJdbcConnectionsMin();
        metricsService.fetchAndSaveJvmBufferCount();
        metricsService.fetchAndSaveJvmBufferMemoryUsed();
        metricsService.fetchAndSaveJvmBufferTotalCapacity();
        metricsService.fetchAndSaveJvmClassesLoaded();
        metricsService.fetchAndSaveJvmClassesUnloaded();
        metricsService.fetchAndSaveJvmCompilationTime();
        metricsService.fetchAndSaveJvmGcLiveDataSize();
        metricsService.fetchAndSaveJvmGcMaxDataSize();
        metricsService.fetchAndSaveJvmGcMemoryAllocated();
        metricsService.fetchAndSaveJvmGcMemoryPromoted();
        metricsService.fetchAndSaveJvmGcOverhead();
        metricsService.fetchAndSaveJvmGcPause();
        metricsService.fetchAndSaveJvmInfo();
        metricsService.fetchAndSaveJvmMemoryCommitted();
        metricsService.fetchAndSaveJvmMemoryMax();
        metricsService.fetchAndSaveJvmMemoryUsageAfterGc();
        metricsService.fetchAndSaveJvmMemoryUsed();
        metricsService.fetchAndSaveJvmThreadsDaemon();
        metricsService.fetchAndSaveJvmThreadsLive();
        metricsService.fetchAndSaveJvmThreadsPeak();
        metricsService.fetchAndSaveJvmThreadsStarted();
        metricsService.fetchAndSaveJvmThreadsStates();
        metricsService.fetchAndSaveLettuceCommandCompletion();
        metricsService.fetchAndSaveLettuceCommandFirstresponse();
        metricsService.fetchAndSaveLogbackEvents();
        metricsService.fetchAndSaveProcessCpuUsage();
        metricsService.fetchAndSaveProcessFilesMax();
        metricsService.fetchAndSaveProcessFilesOpen();
        metricsService.fetchAndSaveProcessStartTime();
        metricsService.fetchAndSaveProcessUptime();
        metricsService.fetchAndSaveSpringDataRepositoryInvocations();
        metricsService.fetchAndSaveSystemCpuCount();
        metricsService.fetchAndSaveSystemCpuUsage();
        metricsService.fetchAndSaveSystemLoadAverage1M();

    }
}

