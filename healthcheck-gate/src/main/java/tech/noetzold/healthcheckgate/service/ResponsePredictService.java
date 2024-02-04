package tech.noetzold.healthcheckgate.service;

import jakarta.transaction.Transactional;
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

    @Transactional
    public ResponsePredict createResponsePredict(ResponsePredict responsePredict) {
        return responsePredictRepository.save(responsePredict);
    }

    public List<ResponsePredict> getAllResponsePredicts() {
        return responsePredictRepository.findAll();
    }

    public Optional<ResponsePredict> getResponsePredictById(Long id) {
        return responsePredictRepository.findById(id);
    }

    @Transactional
    public ResponsePredict updateResponsePredict(Long id, ResponsePredict responsePredictDetails) throws Exception {
        ResponsePredict responsePredict = responsePredictRepository.findById(id)
                .orElseThrow(() -> new Exception("ResponsePredict not found for this id :: " + id));

        responsePredict.setFutureRecord(responsePredictDetails.getFutureRecord());
        responsePredict.setFutureDate(responsePredictDetails.getFutureDate());

        return responsePredictRepository.save(responsePredict);
    }

    @Transactional
    public void deleteResponsePredict(Long id) throws Exception {
        ResponsePredict responsePredict = responsePredictRepository.findById(id)
                .orElseThrow(() -> new Exception("ResponsePredict not found for this id :: " + id));

        responsePredictRepository.delete(responsePredict);
    }
}
