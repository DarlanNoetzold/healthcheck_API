package tech.noetzold.healthcheckgate.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponseAlertDTO {

    @JsonProperty("is_alert")
    boolean isAlert;
}
