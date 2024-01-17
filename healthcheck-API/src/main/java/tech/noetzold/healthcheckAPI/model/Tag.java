package tech.noetzold.healthcheckAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tag;

    @ElementCollection
    @CollectionTable(name = "tag_values", joinColumns = @JoinColumn(name = "tag_id"))
    @Column(name = "value")
    private List<String> values;

    @ManyToOne
    @JoinColumn(name = "metric_response_id")
    private MetricResponse metricResponse;

}