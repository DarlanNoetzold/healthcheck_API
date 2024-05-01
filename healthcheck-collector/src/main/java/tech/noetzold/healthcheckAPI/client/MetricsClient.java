package tech.noetzold.healthcheckAPI.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import tech.noetzold.healthcheckAPI.model.MetricResponse;

@FeignClient(name = "metricsClient", url = "http://192.168.18.18:8193")
public interface MetricsClient {

    @GetMapping("/actuator/jvmDetails")
    MetricResponse getJvmDetails();

    @GetMapping("/actuator/metrics/application.ready.time")
    MetricResponse getApplicationReadyTime();

    @GetMapping("/actuator/metrics/application.started.time")
    MetricResponse getApplicationStartedTime();

    @GetMapping("/actuator/metrics/cache.gets")
    MetricResponse getCacheGets();

    @GetMapping("/actuator/metrics/cache.lock.duration")
    MetricResponse getCacheLockDuration();

    @GetMapping("/actuator/metrics/cache.puts")
    MetricResponse getCachePuts();

    @GetMapping("/actuator/metrics/cache.removals")
    MetricResponse getCacheRemovals();

    @GetMapping("/actuator/metrics/disk.free")
    MetricResponse getDiskFree();

    @GetMapping("/actuator/metrics/disk.total")
    MetricResponse getDiskTotal();

    @GetMapping("/actuator/metrics/executor.active")
    MetricResponse getExecutorActive();

    @GetMapping("/actuator/metrics/executor.completed")
    MetricResponse getExecutorCompleted();

    @GetMapping("/actuator/metrics/executor.pool.core")
    MetricResponse getExecutorPoolCore();

    @GetMapping("/actuator/metrics/executor.pool.max")
    MetricResponse getExecutorPoolMax();

    @GetMapping("/actuator/metrics/executor.pool.size")
    MetricResponse getExecutorPoolSize();

    @GetMapping("/actuator/metrics/executor.queue.remaining")
    MetricResponse getExecutorQueueRemaining();

    @GetMapping("/actuator/metrics/executor.queued")
    MetricResponse getExecutorQueued();

    @GetMapping("/actuator/metrics/hikaricp.connections")
    MetricResponse getHikaricpConnections();

    @GetMapping("/actuator/metrics/hikaricp.connections.acquire")
    MetricResponse getHikaricpConnectionsAcquire();

    @GetMapping("/actuator/metrics/hikaricp.connections.active")
    MetricResponse getHikaricpConnectionsActive();

    @GetMapping("/actuator/metrics/hikaricp.connections.creation")
    MetricResponse getHikaricpConnectionsCreation();

    @GetMapping("/actuator/metrics/hikaricp.connections.idle")
    MetricResponse getHikaricpConnectionsIdle();

    @GetMapping("/actuator/metrics/hikaricp.connections.max")
    MetricResponse getHikaricpConnectionsMax();

    @GetMapping("/actuator/metrics/hikaricp.connections.min")
    MetricResponse getHikaricpConnectionsMin();

    @GetMapping("/actuator/metrics/hikaricp.connections.pending")
    MetricResponse getHikaricpConnectionsPending();

    @GetMapping("/actuator/metrics/hikaricp.connections.timeout")
    MetricResponse getHikaricpConnectionsTimeout();

    @GetMapping("/actuator/metrics/hikaricp.connections.usage")
    MetricResponse getHikaricpConnectionsUsage();

    @GetMapping("/actuator/metrics/http.server.requests")
    MetricResponse getHttpServerRequests();

    @GetMapping("/actuator/metrics/http.server.requests.active")
    MetricResponse getHttpServerRequestsActive();

    @GetMapping("/actuator/metrics/jdbc.connections.active")
    MetricResponse getJdbcConnectionsActive();

    @GetMapping("/actuator/metrics/jdbc.connections.idle")
    MetricResponse getJdbcConnectionsIdle();

    @GetMapping("/actuator/metrics/jdbc.connections.max")
    MetricResponse getJdbcConnectionsMax();

    @GetMapping("/actuator/metrics/jdbc.connections.min")
    MetricResponse getJdbcConnectionsMin();

    @GetMapping("/actuator/metrics/jvm.buffer.count")
    MetricResponse getJvmBufferCount();

    @GetMapping("/actuator/metrics/jvm.buffer.memory.used")
    MetricResponse getJvmBufferMemoryUsed();

    @GetMapping("/actuator/metrics/jvm.buffer.total.capacity")
    MetricResponse getJvmBufferTotalCapacity();

    @GetMapping("/actuator/metrics/jvm.classes.loaded")
    MetricResponse getJvmClassesLoaded();

    @GetMapping("/actuator/metrics/jvm.classes.unloaded")
    MetricResponse getJvmClassesUnloaded();

    @GetMapping("/actuator/metrics/jvm.compilation.time")
    MetricResponse getJvmCompilationTime();

    @GetMapping("/actuator/metrics/jvm.gc.live.data.size")
    MetricResponse getJvmGcLiveDataSize();

    @GetMapping("/actuator/metrics/jvm.gc.max.data.size")
    MetricResponse getJvmGcMaxDataSize();

    @GetMapping("/actuator/metrics/jvm.gc.memory.allocated")
    MetricResponse getJvmGcMemoryAllocated();

    @GetMapping("/actuator/metrics/jvm.gc.memory.promoted")
    MetricResponse getJvmGcMemoryPromoted();

    @GetMapping("/actuator/metrics/jvm.gc.overhead")
    MetricResponse getJvmGcOverhead();

    @GetMapping("/actuator/metrics/jvm.gc.pause")
    MetricResponse getJvmGcPause();

    @GetMapping("/actuator/metrics/jvm.info")
    MetricResponse getJvmInfo();

    @GetMapping("/actuator/metrics/jvm.memory.committed")
    MetricResponse getJvmMemoryCommitted();

    @GetMapping("/actuator/metrics/jvm.memory.max")
    MetricResponse getJvmMemoryMax();

    @GetMapping("/actuator/metrics/jvm.memory.usage.after.gc")
    MetricResponse getJvmMemoryUsageAfterGc();

    @GetMapping("/actuator/metrics/jvm.memory.used")
    MetricResponse getJvmMemoryUsed();

    @GetMapping("/actuator/metrics/jvm.threads.daemon")
    MetricResponse getJvmThreadsDaemon();

    @GetMapping("/actuator/metrics/jvm.threads.live")
    MetricResponse getJvmThreadsLive();

    @GetMapping("/actuator/metrics/jvm.threads.peak")
    MetricResponse getJvmThreadsPeak();

    @GetMapping("/actuator/metrics/jvm.threads.started")
    MetricResponse getJvmThreadsStarted();

    @GetMapping("/actuator/metrics/jvm.threads.states")
    MetricResponse getJvmThreadsStates();

    @GetMapping("/actuator/metrics/lettuce.command.completion")
    MetricResponse getLettuceCommandCompletion();

    @GetMapping("/actuator/metrics/lettuce.command.firstresponse")
    MetricResponse getLettuceCommandFirstresponse();

    @GetMapping("/actuator/metrics/logback.events")
    MetricResponse getLogbackEvents();

    @GetMapping("/actuator/metrics/process.cpu.usage")
    MetricResponse getProcessCpuUsage();

    //@GetMapping("/actuator/metrics/process.files.max")
    //MetricResponse getProcessFilesMax();

    @GetMapping("/actuator/metrics/process.files.open")
    MetricResponse getProcessFilesOpen();

    @GetMapping("/actuator/metrics/process.start.time")
    MetricResponse getProcessStartTime();

    @GetMapping("/actuator/metrics/process.uptime")
    MetricResponse getProcessUptime();

    @GetMapping("/actuator/metrics/spring.data.repository.invocations")
    MetricResponse getSpringDataRepositoryInvocations();

    @GetMapping("/actuator/metrics/system.cpu.count")
    MetricResponse getSystemCpuCount();

    @GetMapping("/actuator/metrics/system.cpu.usage")
    MetricResponse getSystemCpuUsage();

    @GetMapping("/actuator/metrics/system.load.average.1m")
    MetricResponse getSystemLoadAverage1M();

}
