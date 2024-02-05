package tech.noetzold.healthcheckgate.service;


import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.noetzold.healthcheckgate.model.Record;
import tech.noetzold.healthcheckgate.repository.RecordRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecordService {

    @Autowired
    RecordRepository recordRepository;

    private static final Logger logger = LoggerFactory.getLogger(RecordService.class);

    @Transactional
    public Record createRecord(Record record) {
        logger.info("Create a record: {}", record.toString());
        return recordRepository.save(record);
    }

    public List<Record> getAllRecords() {
        logger.info("Finding records");
        return recordRepository.findAll();
    }

    public Optional<Record> getRecordById(Long id) {
        logger.info("Finding record by id: {}", id);
        return recordRepository.findById(id);
    }

    @Transactional
    public Record updateRecord(Long id, Record recordDetails) throws Exception {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new Exception("Record not found for this id :: " + id));

        logger.info("Update record: {}", recordDetails.toString());

        record.setProperty(recordDetails.getProperty());
        record.setStatus(recordDetails.getStatus());
        record.setValue(recordDetails.getValue());

        return recordRepository.save(record);
    }

    @Transactional
    public void deleteRecord(Long id) throws Exception {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new Exception("Record not found for this id :: " + id));

        logger.info("Delete record wit id: {}", id);
        recordRepository.delete(record);
    }



}
