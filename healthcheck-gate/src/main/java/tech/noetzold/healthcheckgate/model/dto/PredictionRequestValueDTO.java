package tech.noetzold.healthcheckgate.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequestValueDTO {
    private String name;
    private List<Double> values;
}
