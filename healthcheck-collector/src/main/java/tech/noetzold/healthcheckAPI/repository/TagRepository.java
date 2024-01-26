package tech.noetzold.healthcheckAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.noetzold.healthcheckAPI.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}