package tech.noetzold.healthcheckgate.service;


import jakarta.transaction.Transactional;
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

    @Transactional
    public Record createRecord(Record record) {
        return recordRepository.save(record);
    }

    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    public Optional<Record> getRecordById(Long id) {
        return recordRepository.findById(id);
    }

    @Transactional
    public Record updateRecord(Long id, Record recordDetails) throws Exception {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new Exception("Record not found for this id :: " + id));

        record.setProperty(recordDetails.getProperty());
        record.setStatus(recordDetails.getStatus());
        record.setValue(recordDetails.getValue());

        return recordRepository.save(record);
    }

    @Transactional
    public void deleteRecord(Long id) throws Exception {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new Exception("Record not found for this id :: " + id));

        recordRepository.delete(record);
    }



}
