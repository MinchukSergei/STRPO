package by.issoft.service;


import by.issoft.entity.NNInitialWeights;

public interface NNInitialWeightsService {
    NNInitialWeights findByUserId(Long userId);
    NNInitialWeights findOne(Long id);
    NNInitialWeights save(NNInitialWeights nnInitialWeights);
}
