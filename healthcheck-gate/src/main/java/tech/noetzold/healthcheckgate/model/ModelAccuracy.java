package tech.noetzold.healthcheckgate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "model_accuracy", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"modelName", "metricName", "accuracyName"})
})
public class ModelAccuracy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String modelName;
    private String accuracyName;
    private String metricName;
    private double accuracyValue;
    private Date trainingDate;

}

