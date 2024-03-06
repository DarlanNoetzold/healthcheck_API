package tech.noetzold.healthcheckgate.controller;

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
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
public class ModelAccuracyController {

    @Autowired
    private ModelAccuracyRepository modelAccuracyRepository;

    @Autowired
    private ModelAccuracyService modelAccuracyService;


    @GetMapping
    public List<ModelAccuracy> getAllModelAccuracies() {
        return modelAccuracyRepository.findAll();
    }

    @PostMapping
    public ModelAccuracy createModelAccuracy(@RequestBody ModelAccuracy modelAccuracy) {
        return modelAccuracyService.upsertModelAccuracy(modelAccuracy);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelAccuracy> getModelAccuracyById(@PathVariable Long id) {
        return modelAccuracyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelAccuracy> updateModelAccuracy(@PathVariable Long id, @RequestBody ModelAccuracy modelAccuracyDetails) {
        return modelAccuracyRepository.findById(id)
                .map(modelAccuracy -> {
                    modelAccuracy.setModelName(modelAccuracyDetails.getModelName());
                    modelAccuracy.setMetricName(modelAccuracyDetails.getMetricName());
                    modelAccuracy.setAccuracyValue(modelAccuracyDetails.getAccuracyValue());
                    modelAccuracy.setTrainingDate(modelAccuracyDetails.getTrainingDate());
                    return ResponseEntity.ok(modelAccuracyRepository.save(modelAccuracy));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModelAccuracy(@PathVariable Long id) {
        return modelAccuracyRepository.findById(id)
                .map(modelAccuracy -> {
                    modelAccuracyRepository.delete(modelAccuracy);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
