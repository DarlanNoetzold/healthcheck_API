package tech.noetzold.healthcheckgate.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponseValueDTO {

    @JsonProperty("GradientBoostingRegressor")
    private Double resultPredictGradientBoostingRegressor;

    @JsonProperty("RandomForestRegressor")
    private Double resultPredictRandomForestRegressor;

    @JsonProperty("SVR")
    private Double resultPredictSVR;

}
