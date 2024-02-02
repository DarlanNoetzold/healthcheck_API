package tech.noetzold.healthcheckgate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.noetzold.healthcheckgate.repository.ResponsePredictRepository;

@Service
public class ResponsePredictService {

    @Autowired
    ResponsePredictRepository responsePredictRepository;
}
