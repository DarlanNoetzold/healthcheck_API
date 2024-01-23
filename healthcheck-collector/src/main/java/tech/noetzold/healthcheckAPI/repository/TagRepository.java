package tech.noetzold.healthcheckAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.healthcheckAPI.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}