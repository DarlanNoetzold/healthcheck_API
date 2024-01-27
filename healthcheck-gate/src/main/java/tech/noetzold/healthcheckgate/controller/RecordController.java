package tech.noetzold.healthcheckgate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.healthcheckgate.model.Record;
import tech.noetzold.healthcheckgate.service.RecordService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/gate")
public class RecordController {

    @Autowired
    RecordService recordService;

    // Create a new record
    @PostMapping("/records")
    public Record createRecord(@RequestBody Record record) {
        return recordService.createRecord(record);
    }

    // Retrieve all records
    @GetMapping("/records")
    public List<Record> getAllRecords() {
        return recordService.getAllRecords();
    }

    // Retrieve a single record by ID
    @GetMapping("/records/{id}")
    public ResponseEntity<Record> getRecordById(@PathVariable(value = "id") Long recordId) throws Exception {
        Record record = recordService.getRecordById(recordId)
                .orElseThrow(() -> new Exception("Record not found for this id :: " + recordId));
        return ResponseEntity.ok().body(record);
    }

    // Update a record
    @PutMapping("/records/{id}")
    public ResponseEntity<Record> updateRecord(@PathVariable(value = "id") Long recordId,
                                               @RequestBody Record recordDetails) throws Exception {
        Record updatedRecord = recordService.updateRecord(recordId, recordDetails);
        return ResponseEntity.ok(updatedRecord);
    }

    // Delete a record
    @DeleteMapping("/records/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable(value = "id") Long recordId) throws Exception {
        recordService.deleteRecord(recordId);
        return ResponseEntity.ok().build();
    }
}
