package tech.noetzold.healthcheckAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String statistic;
    private double value;

    @ManyToOne
    @JoinColumn(name = "metric_response_id")
    private MetricResponse metricResponse;
}