package tech.noetzold.healthcheckAPI.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricResponseGroupedDTO {
    private String name;
    private List<MetricResponse> metricResponses;
}
