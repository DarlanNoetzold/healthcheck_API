package tech.noetzold.healthcheckgate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);

    @PostMapping("/records")
    public ResponseEntity<FutureRecord> createRecord(@RequestBody FutureRecord futureRecord) {
        logger.info("Creating a new record: {}", futureRecord);
        rabbitmqService.sendMessage(RabbitmqQueues.RECORD_QUEUE, futureRecord);
        logger.info("Record created successfully: {}", futureRecord);
        return new ResponseEntity<>(futureRecord, HttpStatus.CREATED);
    }

    @GetMapping("/records")
    public List<FutureRecord> getAllRecords() {
        logger.info("Fetching all records");
        return recordService.getAllRecords();
    }

    @GetMapping("/records/{id}")
    public ResponseEntity<FutureRecord> getRecordById(@PathVariable(value = "id") Long recordId) throws Exception {
        logger.info("Fetching record by id: {}", recordId);
        FutureRecord futureRecord = recordService.getRecordById(recordId)
                .orElseThrow(() -> new Exception("FutureRecord not found for this id :: " + recordId));
        logger.info("Record found: {}", futureRecord);
        return ResponseEntity.ok().body(futureRecord);
    }

    @PutMapping("/records/{id}")
    public ResponseEntity<FutureRecord> updateRecord(@PathVariable(value = "id") Long recordId,
                                                     @RequestBody FutureRecord futureRecordDetails) throws Exception {
        logger.info("Updating record: {} with data: {}", recordId, futureRecordDetails);
        FutureRecord updatedFutureRecord = recordService.updateRecord(recordId, futureRecordDetails);
        logger.info("Record updated successfully: {}", updatedFutureRecord);
        return ResponseEntity.ok(updatedFutureRecord);
    }

    @DeleteMapping("/records/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable(value = "id") Long recordId) throws Exception {
        logger.info("Deleting record by id: {}", recordId);
        recordService.deleteRecord(recordId);
        logger.info("Record deleted successfully: {}", recordId);
        return ResponseEntity.ok().build();
    }

}
