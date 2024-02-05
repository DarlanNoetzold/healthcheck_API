package tech.noetzold.healthcheckgate.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.noetzold.healthcheckgate.model.ResponsePredict;
import tech.noetzold.healthcheckgate.repository.ResponsePredictRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ResponsePredictService {

    @Autowired
    private ResponsePredictRepository responsePredictRepository;

    private static final Logger logger = LoggerFactory.getLogger(ResponsePredictService.class);

    @Transactional
    public ResponsePredict createResponsePredict(ResponsePredict responsePredict) {
        logger.info("Create a ResponsePredict Predict: {}", responsePredict.toString());
        return responsePredictRepository.save(responsePredict);
    }

    public List<ResponsePredict> getAllResponsePredicts() {
        logger.info("Finding ResponsePredicts");
        return responsePredictRepository.findAll();
    }

    public Optional<ResponsePredict> getResponsePredictById(Long id) {
        logger.info("Finding ResponsePredict by id: {}", id);
        return responsePredictRepository.findById(id);
    }

    @Transactional
    public ResponsePredict updateResponsePredict(Long id, ResponsePredict responsePredictDetails) throws Exception {
        ResponsePredict responsePredict = responsePredictRepository.findById(id)
                .orElseThrow(() -> new Exception("ResponsePredict not found for this id :: " + id));

        logger.info("Update ResponsePredict: {}", responsePredictDetails.toString());
        responsePredict.setFutureRecord(responsePredictDetails.getFutureRecord());
        responsePredict.setFutureDate(responsePredictDetails.getFutureDate());

        return responsePredictRepository.save(responsePredict);
    }

    @Transactional
    public void deleteResponsePredict(Long id) throws Exception {
        ResponsePredict responsePredict = responsePredictRepository.findById(id)
                .orElseThrow(() -> new Exception("ResponsePredict not found for this id :: " + id));

        logger.info("Delete ResponsePredict by id: {}", id);
        responsePredictRepository.delete(responsePredict);
    }
}
