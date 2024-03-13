package tech.noetzold.healthcheckgate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.healthcheckgate.message.config.RabbitmqQueues;
import tech.noetzold.healthcheckgate.model.FutureRecord;
import tech.noetzold.healthcheckgate.service.RabbitmqService;
import tech.noetzold.healthcheckgate.service.RecordService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/healthcheck/v1/gate")
@PreAuthorize("hasAnyRole('ADMIN')")
public class RecordController {

    @Autowired
    RecordService recordService;

    @Autowired
    private RabbitmqService rabbitmqService;

    @PostMapping("/records")
    public ResponseEntity<FutureRecord> createRecord(@RequestBody FutureRecord futureRecord) {
        rabbitmqService.sendMessage(RabbitmqQueues.RECORD_QUEUE, futureRecord);
        return new ResponseEntity<FutureRecord>(futureRecord, HttpStatus.CREATED);
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
