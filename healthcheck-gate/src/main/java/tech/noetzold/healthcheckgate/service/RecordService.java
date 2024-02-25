package tech.noetzold.healthcheckgate.service;


import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.noetzold.healthcheckgate.clients.PredictClient;
import tech.noetzold.healthcheckgate.model.FutureRecord;
import tech.noetzold.healthcheckgate.model.Status;
import tech.noetzold.healthcheckgate.model.dto.PredictionRequestAlertDTO;
import tech.noetzold.healthcheckgate.model.dto.PredictionRequestValueDTO;
import tech.noetzold.healthcheckgate.model.dto.PredictionResponseAlertDTO;
import tech.noetzold.healthcheckgate.model.dto.PredictionResponseValueDTO;
import tech.noetzold.healthcheckgate.repository.RecordRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    PredictClient predictClient;

    private static final Logger logger = LoggerFactory.getLogger(RecordService.class);

    @Transactional
    public FutureRecord createRecord(FutureRecord futureRecord) {
        logger.info("Create a futureRecord: {}", futureRecord.toString());
        return recordRepository.save(buildEntityFromRequests(futureRecord));
    }

    public List<FutureRecord> getAllRecords() {
        logger.info("Finding records");
        return recordRepository.findAll();
    }

    public Optional<FutureRecord> getRecordById(Long id) {
        logger.info("Finding record by id: {}", id);
        return recordRepository.findById(id);
    }

    @Transactional
    public FutureRecord updateRecord(Long id, FutureRecord futureRecordDetails) throws Exception {
        FutureRecord futureRecord = recordRepository.findById(id)
                .orElseThrow(() -> new Exception("FutureRecord not found for this id :: " + id));

        logger.info("Update futureRecord: {}", futureRecordDetails.toString());

        futureRecord = buildEntityFromRequests(futureRecordDetails);

        return recordRepository.save(futureRecord);
    }

    @Transactional
    public void deleteRecord(Long id) throws Exception {
        FutureRecord futureRecord = recordRepository.findById(id)
                .orElseThrow(() -> new Exception("FutureRecord not found for this id :: " + id));

        logger.info("Delete futureRecord wit id: {}", id);
        recordRepository.delete(futureRecord);
    }

    private FutureRecord buildEntityFromRequests(FutureRecord futureRecord) {
        try {
            PredictionResponseValueDTO predictionResponseValueDTO = predictClient.predictValue(
                    new PredictionRequestValueDTO(futureRecord.getPropertyName(), futureRecord.getValues()));
            if (predictionResponseValueDTO == null
                    || predictionResponseValueDTO.getResultPredictSVR() == null
                    || predictionResponseValueDTO.getResultPredictRandomForestRegressor() == null
                    || predictionResponseValueDTO.getResultPredictGradientBoostingRegressor() == null) {
                throw new Exception("Falha ao receber previsão de valor da API Python.");
            }

            PredictionResponseAlertDTO predictionResponseAlertDTO = predictClient.predictAlert(
                    new PredictionRequestAlertDTO(futureRecord.getPropertyName(), predictionResponseValueDTO.getResultPredictRandomForestRegressor()));
            if (predictionResponseAlertDTO == null) {
                throw new Exception("Falha ao receber previsão de alerta da API Python.");
            }

            if (predictionResponseAlertDTO.isAlertLogisticRegression()) {
                futureRecord.setFutureStatusLogisticRegression(Status.IS_ALERT);
            } else {
                futureRecord.setFutureStatusLogisticRegression(Status.NOT_ALERT);
            }

            if (predictionResponseAlertDTO.isAlertRandomForestClassifier()) {
                futureRecord.setFutureStatusRandomForestClassifier(Status.IS_ALERT);
            } else {
                futureRecord.setFutureStatusRandomForestClassifier(Status.NOT_ALERT);
            }

            if (predictionResponseAlertDTO.isAlertGradientBoostingClassifier()) {
                futureRecord.setFutureStatusGradientBoostingClassifier(Status.IS_ALERT);
            } else {
                futureRecord.setFutureStatusGradientBoostingClassifier(Status.NOT_ALERT);
            }

            futureRecord.setPredictionDate(new Date());
            futureRecord.setPredictValueSVR(predictionResponseValueDTO.getResultPredictSVR());
            futureRecord.setPredictValueGradientBoostingRegressor(predictionResponseValueDTO.getResultPredictGradientBoostingRegressor());
            futureRecord.setPredictValueRandomForestRegressor(predictionResponseValueDTO.getResultPredictRandomForestRegressor());

        } catch (Exception e) {
            System.err.println("Erro ao processar previsões: " + e.getMessage());
            futureRecord.setFutureStatusGradientBoostingClassifier(Status.ERROR);
            futureRecord.setFutureStatusLogisticRegression(Status.ERROR);
            futureRecord.setFutureStatusRandomForestClassifier(Status.ERROR);
        }

        return futureRecord;
    }

}
