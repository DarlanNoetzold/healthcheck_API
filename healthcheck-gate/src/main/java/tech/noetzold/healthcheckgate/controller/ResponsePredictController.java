package tech.noetzold.healthcheckgate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.healthcheckgate.model.ResponsePredict;
import tech.noetzold.healthcheckgate.service.ResponsePredictService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/predict")
public class ResponsePredictController {

    @Autowired
    private ResponsePredictService responsePredictService;

    @PostMapping
    public ResponseEntity<ResponsePredict> createResponsePredict(@RequestBody ResponsePredict responsePredict) {
        return ResponseEntity.ok(responsePredictService.createResponsePredict(responsePredict));
    }

    @GetMapping
    public ResponseEntity<List<ResponsePredict>> getAllResponsePredicts() {
        return ResponseEntity.ok(responsePredictService.getAllResponsePredicts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ResponsePredict>> getResponsePredictById(@PathVariable Long id) {
        Optional<ResponsePredict> responsePredict = responsePredictService.getResponsePredictById(id);
        if(responsePredict.isPresent()) {
            return ResponseEntity.ok(responsePredict);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePredict> updateResponsePredict(@PathVariable Long id, @RequestBody ResponsePredict responsePredictDetails) {
        try {
            ResponsePredict updatedResponsePredict = responsePredictService.updateResponsePredict(id, responsePredictDetails);
            return ResponseEntity.ok(updatedResponsePredict);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResponsePredict(@PathVariable Long id) {
        try {
            responsePredictService.deleteResponsePredict(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
