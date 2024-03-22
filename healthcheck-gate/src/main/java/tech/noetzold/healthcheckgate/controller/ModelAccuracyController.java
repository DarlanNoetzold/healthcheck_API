package tech.noetzold.healthcheckgate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.healthcheckgate.model.ModelAccuracy;
import tech.noetzold.healthcheckgate.repository.ModelAccuracyRepository;
import tech.noetzold.healthcheckgate.service.ModelAccuracyService;

import java.util.List;

@RestController
@RequestMapping("/healthcheck/v1/gate/model-accuracies")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('ADMIN')")
public class ModelAccuracyController {

    @Autowired
    private ModelAccuracyRepository modelAccuracyRepository;

    @Autowired
    private ModelAccuracyService modelAccuracyService;

    private static final Logger logger = LoggerFactory.getLogger(ModelAccuracyController.class);

    @GetMapping
    public List<ModelAccuracy> getAllModelAccuracies() {
        logger.info("Fetching all model accuracies");
        return modelAccuracyRepository.findAll();
    }

    @PostMapping
    public ModelAccuracy createModelAccuracy(@RequestBody ModelAccuracy modelAccuracy) {
        logger.info("Creating model accuracy: {}", modelAccuracy);
        ModelAccuracy createdModelAccuracy = modelAccuracyService.upsertModelAccuracy(modelAccuracy);
        logger.info("Model accuracy created successfully: {}", createdModelAccuracy);
        return createdModelAccuracy;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelAccuracy> getModelAccuracyById(@PathVariable Long id) {
        logger.info("Fetching model accuracy by id: {}", id);
        return modelAccuracyRepository.findById(id)
                .map(modelAccuracy -> {
                    logger.info("Model accuracy found: {}", modelAccuracy);
                    return ResponseEntity.ok(modelAccuracy);
                })
                .orElseGet(() -> {
                    logger.warn("Model accuracy not found for id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelAccuracy> updateModelAccuracy(@PathVariable Long id, @RequestBody ModelAccuracy modelAccuracyDetails) {
        logger.info("Updating model accuracy: {} with data: {}", id, modelAccuracyDetails);
        return modelAccuracyRepository.findById(id)
                .map(modelAccuracy -> {
                    modelAccuracy.setModelName(modelAccuracyDetails.getModelName());
                    modelAccuracy.setMetricName(modelAccuracyDetails.getMetricName());
                    modelAccuracy.setAccuracyValue(modelAccuracyDetails.getAccuracyValue());
                    modelAccuracy.setTrainingDate(modelAccuracyDetails.getTrainingDate());
                    ModelAccuracy updatedModelAccuracy = modelAccuracyRepository.save(modelAccuracy);
                    logger.info("Model accuracy updated successfully: {}", updatedModelAccuracy);
                    return ResponseEntity.ok(updatedModelAccuracy);
                })
                .orElseGet(() -> {
                    logger.warn("Model accuracy not found for update: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModelAccuracy(@PathVariable Long id) {
        logger.info("Deleting model accuracy by id: {}", id);
        return modelAccuracyRepository.findById(id)
                .map(modelAccuracy -> {
                    modelAccuracyRepository.delete(modelAccuracy);
                    logger.info("Model accuracy deleted successfully: {}", id);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> {
                    logger.warn("Model accuracy not found for deletion: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
}
