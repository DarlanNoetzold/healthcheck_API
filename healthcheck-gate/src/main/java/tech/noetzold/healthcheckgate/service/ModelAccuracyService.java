package tech.noetzold.healthcheckgate.service;


import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tech.noetzold.healthcheckgate.model.ModelAccuracy;
import tech.noetzold.healthcheckgate.repository.ModelAccuracyRepository;

import java.util.Optional;

@Service
@Cacheable("accuracy")
public class ModelAccuracyService {

    @Autowired
    private ModelAccuracyRepository modelAccuracyRepository;

    private static final Logger logger = LoggerFactory.getLogger(ModelAccuracyService.class);

    @Transactional
    public ModelAccuracy upsertModelAccuracy(ModelAccuracy newModelAccuracy) {
        Optional<ModelAccuracy> existingModelAccuracy = modelAccuracyRepository.findByModelNameAndMetricNameAndAccuracyName(
                newModelAccuracy.getModelName(), newModelAccuracy.getMetricName(), newModelAccuracy.getAccuracyName());

        logger.info("Returned model: {}", existingModelAccuracy.toString());
        logger.info("Add model: {}", newModelAccuracy.toString());

        if (existingModelAccuracy.isPresent()) {
            ModelAccuracy updatedModelAccuracy = existingModelAccuracy.get();
            updatedModelAccuracy.setAccuracyName(newModelAccuracy.getAccuracyName());
            updatedModelAccuracy.setAccuracyValue(newModelAccuracy.getAccuracyValue());
            updatedModelAccuracy.setTrainingDate(newModelAccuracy.getTrainingDate());
            return modelAccuracyRepository.save(updatedModelAccuracy);
        } else {
            return modelAccuracyRepository.save(newModelAccuracy);
        }
    }
}
