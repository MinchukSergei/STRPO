package by.issoft.service.impl;

import by.issoft.entity.LearningSet;
import by.issoft.entity.NNInitialWeights;
import by.issoft.entity.UserData;
import by.issoft.entity.UserEvent;
import by.issoft.repository.LearningSetRepository;
import by.issoft.service.LearningSetService;
import by.issoft.service.NNInitialWeightsService;
import org.apache.commons.lang.ArrayUtils;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import util.Converter;

import java.util.*;

@Service
public class LearningSetServiceImpl implements LearningSetService {

    private final LearningSetRepository learningSetRepository;
    private final NNInitialWeightsService nnInitialWeightsService;

    @Autowired
    public LearningSetServiceImpl(LearningSetRepository learningSetRepository, NNInitialWeightsService nnInitialWeightsService) {
        this.learningSetRepository = learningSetRepository;
        this.nnInitialWeightsService = nnInitialWeightsService;
    }

    @Override
    public Set<LearningSet> findByUserId(Long userId) {
        return learningSetRepository.findByUserId(userId);
    }

    @Override
    public Set<LearningSet> findByUserIdAndEventId(Long userId, Long eventId) {
        return learningSetRepository.findByUserIdAndEventId(userId, eventId);
    }

    @Override
    public void delete(Long id) {
        learningSetRepository.delete(id);
    }

    @Override
    public LearningSet add(LearningSet learningSet) {
        return learningSetRepository.save(learningSet);
    }

    @Override
    public void learnNeuralNetwork(UserData currentUser) {
        new Thread(() -> {
            int iterations = 100000;
            double maxError = 0.005;
            int inputSize = 5;
            int outputSize = 1;

            List<Integer> layers = Arrays.asList(inputSize, 4, outputSize);
            MultiLayerPerceptron mlp = new MultiLayerPerceptron(layers, TransferFunctionType.SIGMOID);
            mlp.getLearningRule().setMaxIterations(iterations);
            mlp.getLearningRule().setMaxError(maxError);

            NNInitialWeights nnInitialWeights = nnInitialWeightsService.findByUserId(currentUser.getId());
            if (nnInitialWeights != null) {
                mlp.setWeights(Converter.toDoubleArray(nnInitialWeights.getInitialWeights()));
            } else {
                nnInitialWeights = new NNInitialWeights();
                nnInitialWeights.setInitialWeights(Converter.toByteArray(ArrayUtils.toPrimitive(mlp.getWeights())));
                nnInitialWeights.setUserId(currentUser.getId());
                nnInitialWeights = nnInitialWeightsService.save(nnInitialWeights);
            }

            DataSet learningDataSet = new DataSet(inputSize, outputSize);

            Set<LearningSet> learningSets = learningSetRepository.findByUserId(currentUser.getId());
            for (LearningSet learningSet : learningSets) {
                learningDataSet.addRow(Converter.getInput(learningSet), Converter.getOutput(learningSet));
            }

            Normalizer normalizer = new MaxNormalizer();
            normalizer.normalize(learningDataSet);

            long first = System.currentTimeMillis();
            mlp.learn(learningDataSet);

            System.out.println(System.currentTimeMillis() - first + "=================================");
            nnInitialWeights.setInitialWeights(Converter.toByteArray(ArrayUtils.toPrimitive(mlp.getWeights())));
            nnInitialWeightsService.save(nnInitialWeights);
        }).start();
    }
}
