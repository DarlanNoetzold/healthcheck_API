package tech.noetzold.healthcheckgate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.healthcheckgate.model.FutureRecord;
import tech.noetzold.healthcheckgate.service.RecordService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/gate")
public class RecordController {

    @Autowired
    RecordService recordService;

    @PostMapping("/records")
    public FutureRecord createRecord(@RequestBody FutureRecord futureRecord) {
        return recordService.createRecord(futureRecord);
    }

    @GetMapping("/records")
    public List<FutureRecord> getAllRecords() {
        return recordService.getAllRecords();
    }

    @GetMapping("/records/{id}")
    public ResponseEntity<FutureRecord> getRecordById(@PathVariable(value = "id") Long recordId) throws Exception {
        FutureRecord futureRecord = recordService.getRecordById(recordId)
                .orElseThrow(() -> new Exception("FutureRecord not found for this id :: " + recordId));
        return ResponseEntity.ok().body(futureRecord);
    }

    @PutMapping("/records/{id}")
    public ResponseEntity<FutureRecord> updateRecord(@PathVariable(value = "id") Long recordId,
                                                     @RequestBody FutureRecord futureRecordDetails) throws Exception {
        FutureRecord updatedFutureRecord = recordService.updateRecord(recordId, futureRecordDetails);
        return ResponseEntity.ok(updatedFutureRecord);
    }

    @DeleteMapping("/records/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable(value = "id") Long recordId) throws Exception {
        recordService.deleteRecord(recordId);
        return ResponseEntity.ok().build();
    }
}
