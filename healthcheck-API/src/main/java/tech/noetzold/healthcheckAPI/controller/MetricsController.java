package tech.noetzold.healthcheckAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.noetzold.healthcheckAPI.model.MetricResponse;
import tech.noetzold.healthcheckAPI.model.MetricResponseGroupedDTO;
import tech.noetzold.healthcheckAPI.repository.MetricsRepository;
import tech.noetzold.healthcheckAPI.service.MetricsService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MetricsController {

    @Autowired
    MetricsRepository metricsRepository;

    @Autowired
    MetricsService metricsService;

    @GetMapping("/ping")
    public String ping(){
        return "API is up";
    }

    @GetMapping("/all")
    public List<MetricResponse> getAllMetrics(){
        return metricsRepository.findAll();
    }

    @GetMapping("/all/byname")
    public List<MetricResponseGroupedDTO> getAllMetricsGroupByName(){
        return metricsService.getMetricsGroupedByName();
    }

}
