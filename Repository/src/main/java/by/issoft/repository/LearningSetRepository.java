package by.issoft.repository;

import by.issoft.entity.LearningSet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LearningSetRepository extends CrudRepository<LearningSet, Long> {
    Set<LearningSet> findByUserId(Long userId);
    Set<LearningSet> findByUserIdAndEventId(Long userId, Long eventId);

}
