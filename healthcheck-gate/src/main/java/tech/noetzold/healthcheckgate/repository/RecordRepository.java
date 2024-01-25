package tech.noetzold.healthcheckgate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.healthcheckgate.model.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
