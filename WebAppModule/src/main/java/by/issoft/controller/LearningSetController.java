package by.issoft.controller;

import by.issoft.entity.LearningSet;
import by.issoft.entity.NNInitialWeights;
import by.issoft.entity.UserData;
import by.issoft.entity.UserEvent;
import by.issoft.service.LearningSetService;
import by.issoft.service.NNInitialWeightsService;
import by.issoft.service.UserDataService;
import by.issoft.service.UserEventService;
import by.issoft.session.SessionManager;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;
import util.Converter;
import org.apache.commons.lang.ArrayUtils;
import org.neuroph.core.data.DataSetRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
@RequestMapping("/learning")
public class LearningSetController {
    private final SessionManager sessionManager;
    private final UserDataService userDataService;
    private final UserEventService userEventService;
    private final LearningSetService learningSetService;
    private final NNInitialWeightsService nnInitialWeightsService;

    @Autowired
    public LearningSetController(SessionManager sessionManager, UserDataService userDataService, UserEventService userEventService, LearningSetService learningSetService, NNInitialWeightsService nnInitialWeightsService) {
        this.sessionManager = sessionManager;
        this.userDataService = userDataService;
        this.userEventService = userEventService;
        this.learningSetService = learningSetService;
        this.nnInitialWeightsService = nnInitialWeightsService;
    }

    @RequestMapping(value = "/{accepted}", method = RequestMethod.POST)
    public ResponseEntity addLearningRow(@RequestBody UserEvent event, @PathVariable("accepted") Boolean accepted) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;

        UserEvent userEvent = userEventService.findById(event.getId());

        if (userEvent == null) {
            status = HttpStatus.BAD_REQUEST;
        } else if (!userEvent.getPublic()) {
            status = HttpStatus.FORBIDDEN;
        } else {
            UserData eventOwner = userEvent.getUserOwner();
            UserData eventOwnerFetchFriends = userDataService.findByIdAndFetchFriendsList(eventOwner.getId());

            if (eventOwnerFetchFriends == null || !eventOwnerFetchFriends.getFriends().contains(currentUser)) {
                status = HttpStatus.FORBIDDEN;
            } else {
                UserEvent userEventFetchEventMembers = userEventService.findByIdAndFetchMembers(userEvent.getId());
                Set<UserData> eventMembers;
                if (userEventFetchEventMembers == null) {
                    eventMembers = new HashSet<>();
                } else {
                    eventMembers = userEventFetchEventMembers.getEventMembers();
                }
                if (accepted) {
                    eventMembers.add(currentUser);
                } else {
                    eventMembers.remove(currentUser);
                }
                userEvent.setEventMembers(eventMembers);
                userEventService.save(userEvent);

                Set<LearningSet> learningSet = learningSetService.findByUserIdAndEventId(currentUser.getId(), event.getId());

                UserData currentUserFetchFriends = userDataService.findByIdAndFetchFriendsList(currentUser.getId());
                Set<UserData> userFriends;
                if (currentUserFetchFriends == null) {
                    userFriends = new HashSet<>();
                } else {
                    userFriends = currentUserFetchFriends.getFriends();
                }

                Set<LearningSet> newLearningSet = new HashSet<>();
                for (UserData member : eventMembers) {
                    if (userFriends.contains(member)) {
                        LearningSet newLearningRow = convertEventToLearningSet(userEvent);
                        newLearningRow.setUserId(currentUser.getId());
                        newLearningRow.setUserParticipant(member.getId());
                        newLearningRow.setAccepted(accepted);
                        newLearningSet.add(newLearningRow);
                    }
                }

                for (LearningSet row : learningSet) {
                    learningSetService.delete(row.getId());
                }

                if (newLearningSet.isEmpty()) {
                    LearningSet newLearningRow = convertEventToLearningSet(userEvent);
                    newLearningRow.setUserId(currentUser.getId());
                    newLearningRow.setUserParticipant(currentUser.getId());
                    newLearningRow.setAccepted(accepted);
                    learningSetService.add(newLearningRow);
                } else {
                    for (LearningSet learningRow : newLearningSet) {
                        learningSetService.add(learningRow);
                    }
                }

                status = HttpStatus.OK;
            }
        }
        return new ResponseEntity(status);
    }



    @RequestMapping(value = "/actual", method = RequestMethod.GET)
    public ResponseEntity getActualEvents() {
        UserData currentUser = sessionManager.getAuthenticatedUser();

        Set<UserEvent> userEvents = userEventService.findPublicEventsOfFriendsInTheFutureFetchMembers(currentUser);
        filterNonActualEvents(userEvents, currentUser);
        List<Boolean> isAcceptedEvents = getAcceptedValues(userEvents, currentUser);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("events", userEvents);
        responseMap.put("accepted", isAcceptedEvents);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    private void filterNonActualEvents(Set<UserEvent> userEvents, UserData currentUser) {
        int inputSize = 5;
        int outputSize = 1;

        DataSet inputDataSet = new DataSet(inputSize);

        for (UserEvent userEvent : userEvents) {
            LearningSet learningSet = convertEventToLearningSet(userEvent);

            if (userEvent.getEventMembers().isEmpty()) {
                learningSet.setUserParticipant(currentUser.getId());
                inputDataSet.addRow(createNewDataSetRow(learningSet, userEvent));
            } else {
                for (UserData member : userEvent.getEventMembers()) {
                    if (!member.equals(currentUser)) {
                        learningSet.setUserParticipant(member.getId());
                        inputDataSet.addRow(createNewDataSetRow(learningSet, userEvent));
                    }
                }
            }
        }

        Map<Long, UserEvent> userEventMap = new HashMap<>();
        for (UserEvent userEvent : userEvents) {
            userEventMap.put(userEvent.getId(), userEvent);
        }

        List<Integer> layers = Arrays.asList(inputSize, 4, outputSize);
        MultiLayerPerceptron mlp = new MultiLayerPerceptron(layers, TransferFunctionType.SIGMOID);
        NNInitialWeights nnInitialWeights = nnInitialWeightsService.findByUserId(currentUser.getId());
        if (nnInitialWeights != null) {
            mlp.setWeights(Converter.toDoubleArray(nnInitialWeights.getInitialWeights()));
        }

        Normalizer normalizer = new MaxNormalizer();
        normalizer.normalize(inputDataSet);

        Map<Long, List<Double>> eventActualProbability = new HashMap<>();
        for (DataSetRow dataSetRow : inputDataSet.getRows()) {
            mlp.setInput(dataSetRow.getInput());
            mlp.calculate();
            double[] output = mlp.getOutput();
            Long id = Long.valueOf(dataSetRow.getLabel());
            eventActualProbability.computeIfAbsent(id, k -> new ArrayList<>());
            eventActualProbability.get(id).add(output[0]);
        }

        for (Map.Entry<Long, List<Double>> entry : eventActualProbability.entrySet()) {
            List<Double> probability = entry.getValue();
            Double result = 0.;
            for (Double prob : probability) {
                result += prob;
            }
            result /= probability.size();
            if (result < 0.5) {
                userEvents.remove(userEventMap.get(entry.getKey()));
            }
        }
    }

    private LearningSet convertEventToLearningSet(UserEvent userEvent) {
        LearningSet newLearningRow = new LearningSet();
        newLearningRow.setEventId(userEvent.getId());
        newLearningRow.setUserOwner(userEvent.getUserOwner().getId());
        newLearningRow.setEventType(userEvent.getEventType().getId());

        Calendar eventTimestamp = userEvent.getEventTimestamp();
        newLearningRow.setEventWeekDay((byte) eventTimestamp.get(Calendar.DAY_OF_WEEK));
        newLearningRow.setEventTime(eventTimestamp.get(Calendar.HOUR_OF_DAY) * 60 + eventTimestamp.get(Calendar.MINUTE));
        return newLearningRow;
    }

    private DataSetRow createNewDataSetRow(LearningSet learningSet, UserEvent userEvent) {
        DataSetRow dataSetRow = new DataSetRow();
        dataSetRow.setLabel(userEvent.getId().toString());
        dataSetRow.setInput(Converter.getInput(learningSet));
        return dataSetRow;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity getAllEvents() {
        UserData currentUser = sessionManager.getAuthenticatedUser();

        Set<UserEvent> userEvents = userEventService.findPublicEventsOfFriendsInTheFutureFetchMembers(currentUser);
        List<Boolean> isAcceptedEvents = getAcceptedValues(userEvents, currentUser);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("events", userEvents);
        responseMap.put("accepted", isAcceptedEvents);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    private List<Boolean> getAcceptedValues(Set<UserEvent> events, UserData currentUser) {
        List<Boolean> isAcceptedEvents = new ArrayList<>();
        for (UserEvent event : events) {
            Set<LearningSet> learningSets = learningSetService.findByUserIdAndEventId(currentUser.getId(), event.getId());

            Boolean accepted = null;
            if (learningSets.iterator().hasNext()) {
                if (!learningSets.iterator().next().getAccepted()) {
                    accepted = false;
                }
            }
            if (event.getEventMembers().contains(currentUser)) {
                accepted = true;
            }
            isAcceptedEvents.add(accepted);
        }
        return isAcceptedEvents;
    }
}
