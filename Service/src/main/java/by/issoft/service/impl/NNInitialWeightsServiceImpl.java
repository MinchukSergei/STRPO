package by.issoft.service.impl;

import by.issoft.entity.NNInitialWeights;
import by.issoft.repository.NNInitialWeightsRepository;
import by.issoft.service.NNInitialWeightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NNInitialWeightsServiceImpl implements NNInitialWeightsService {
    private final NNInitialWeightsRepository initialWeightsRepository;

    @Autowired
    public NNInitialWeightsServiceImpl(NNInitialWeightsRepository initialWeightsRepository) {
        this.initialWeightsRepository = initialWeightsRepository;
    }

    @Override
    public NNInitialWeights findByUserId(Long userId) {
        return initialWeightsRepository.findByUserId(userId);
    }

    @Override
    public NNInitialWeights findOne(Long id) {
        return initialWeightsRepository.findOne(id);
    }

    @Override
    public NNInitialWeights save(NNInitialWeights nnInitialWeights) {
        return initialWeightsRepository.save(nnInitialWeights);
    }
}
