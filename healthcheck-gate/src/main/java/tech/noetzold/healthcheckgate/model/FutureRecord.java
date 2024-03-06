package tech.noetzold.healthcheckgate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FutureRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Status futureStatusGradientBoostingClassifier;
    private Status futureStatusLogisticRegression;
    private Status futureStatusRandomForestClassifier;
    private String propertyName;
    private Double predictValueGradientBoostingRegressor;
    private Double predictValueRandomForestRegressor;
    private Double predictValueSVR;
    private List<Double> values;
    private Date predictionDate;
}
