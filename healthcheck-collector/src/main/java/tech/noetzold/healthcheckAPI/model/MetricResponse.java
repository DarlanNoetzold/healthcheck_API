package tech.noetzold.healthcheckAPI.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MetricResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String baseUnit;

    @Column
    private boolean collected;

    @OneToMany(mappedBy = "metricResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Measurement> measurements = new ArrayList<>();

    @OneToMany(mappedBy = "metricResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Tag> availableTags = new ArrayList<>();

    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
        measurement.setMetricResponse(this);
    }

    public void removeMeasurement(Measurement measurement) {
        measurements.remove(measurement);
        measurement.setMetricResponse(null);
    }

    public void addTag(Tag tag) {
        availableTags.add(tag);
        tag.setMetricResponse(this);
    }

    public void removeTag(Tag tag) {
        availableTags.remove(tag);
        tag.setMetricResponse(null);
    }
}
