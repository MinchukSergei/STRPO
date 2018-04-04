package by.issoft.repository;

import by.issoft.entity.NNInitialWeights;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NNInitialWeightsRepository extends CrudRepository<NNInitialWeights, Long> {
    NNInitialWeights findByUserId(Long userId);
}
