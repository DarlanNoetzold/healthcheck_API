package tech.noetzold.healthcheckgate.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponseAlertDTO {

    @JsonProperty("GradientBoostingClassifier")
    boolean isAlertGradientBoostingClassifier;

    @JsonProperty("LogisticRegression")
    boolean isAlertLogisticRegression;

    @JsonProperty("RandomForestClassifier")
    boolean isAlertRandomForestClassifier;
}
