package tech.noetzold.healthcheckgate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.healthcheckgate.model.FutureRecord;

public interface RecordRepository extends JpaRepository<FutureRecord, Long> {
}
