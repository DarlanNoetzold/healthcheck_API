package tech.noetzold.healthcheckgate.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tech.noetzold.healthcheckgate.model.dto.PredictionRequestAlertDTO;
import tech.noetzold.healthcheckgate.model.dto.PredictionRequestValueDTO;
import tech.noetzold.healthcheckgate.model.dto.PredictionResponseAlertDTO;
import tech.noetzold.healthcheckgate.model.dto.PredictionResponseValueDTO;

@FeignClient(name = "predictClient", url = "http://127.0.0.1:5000")
public interface PredictClient {

    @PostMapping("/predict-value")
    PredictionResponseValueDTO predictValue(@RequestBody PredictionRequestValueDTO requestDTO);

    @PostMapping("/predict-alert")
    PredictionResponseAlertDTO predictAlert(@RequestBody PredictionRequestAlertDTO requestDTO);
}
