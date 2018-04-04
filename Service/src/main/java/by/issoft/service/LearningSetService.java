package by.issoft.service;

import by.issoft.entity.LearningSet;
import by.issoft.entity.UserData;

import java.util.Set;

public interface LearningSetService {
    Set<LearningSet> findByUserId(Long userId);
    Set<LearningSet> findByUserIdAndEventId(Long userId, Long eventId);
    void delete(Long id);
    LearningSet add(LearningSet learningSet);
    void learnNeuralNetwork(UserData userData);
}
