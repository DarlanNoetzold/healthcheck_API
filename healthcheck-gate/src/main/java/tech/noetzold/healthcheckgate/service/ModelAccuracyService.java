package tech.noetzold.healthcheckgate.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.noetzold.healthcheckgate.model.ModelAccuracy;
import tech.noetzold.healthcheckgate.repository.ModelAccuracyRepository;

import java.util.Optional;

@Service
public class ModelAccuracyService {

    @Autowired
    private ModelAccuracyRepository modelAccuracyRepository;

    @Transactional
    public ModelAccuracy upsertModelAccuracy(ModelAccuracy newModelAccuracy) {
        Optional<ModelAccuracy> existingModelAccuracy = modelAccuracyRepository.findByModelNameAndMetricNameAndaccuracyName(
                newModelAccuracy.getModelName(), newModelAccuracy.getMetricName(), newModelAccuracy.getAccuracyName());

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
