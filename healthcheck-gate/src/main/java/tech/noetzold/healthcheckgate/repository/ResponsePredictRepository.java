package tech.noetzold.healthcheckgate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.healthcheckgate.model.ResponsePredict;

public interface ResponsePredictRepository extends JpaRepository<ResponsePredict, Long> {
}
