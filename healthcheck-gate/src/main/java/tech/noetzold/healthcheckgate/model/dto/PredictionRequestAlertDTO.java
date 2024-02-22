package tech.noetzold.healthcheckgate.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequestAlertDTO {
    private String name;
    private Double prediction;
}
