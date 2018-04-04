package by.issoft.service.impl;


import by.issoft.entity.UserData;
import by.issoft.entity.UserEvent;
import by.issoft.repository.UserEventRepository;
import by.issoft.service.UserEventService;
import by.issoft.service.util.EventCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserEventServiceImpl implements UserEventService {
    private final UserEventRepository userEventRepository;

    @Autowired
    public UserEventServiceImpl(UserEventRepository userEventRepository) {
        this.userEventRepository = userEventRepository;
    }

    @Override
    public UserEvent findByIdAndFetchMembers(Long id){
        return userEventRepository.findByIdAndFetchMembers(id);
    }

    @Override
    public Set<UserEvent> findSharedEventsBetweenDatesByUserId(String dateFrom, String dateTo, String format, UserData user) {
        format += " %H:%i:%S";
        dateFrom += " 00:00:00";
        dateTo += " 23:59:59";
        return userEventRepository.findSharedEventsBetweenDatesByUserId(dateFrom, dateTo, format, user);
    }

    @Override
    public List<EventCount> findCountSharedEventsBetweenDatesByUserId(String dateFrom, String dateTo, String format, UserData user) {
        String selectFormat = format;
        format += " %H:%i:%S";
        dateFrom += " 00:00:00";
        dateTo += " 23:59:59";
        Set<Object[]> countSharedEvents = userEventRepository.findCountSharedEventsBetweenDatesByUserId(selectFormat, dateFrom, dateTo, format, user);
        List<EventCount> eventCounts = new ArrayList<>();

        Map<String, EventCount> eventCountMap = new HashMap<>();

        for (Object[] oo : countSharedEvents) {
            String date = (String) oo[1];
            UserData owner = (UserData) oo[2];
            EventCount eventCount = eventCountMap.get(date);
            if (eventCount == null) {
                eventCount = new EventCount();
                eventCount.setDate(date);
            }

            if (owner.getId().equals(user.getId())) {
                eventCount.setOwnCount(eventCount.getOwnCount() + 1);
            } else {
                eventCount.setSharedCount(eventCount.getSharedCount() + 1);
            }
            eventCountMap.put(date, eventCount);
        }

        for (EventCount eventCount : eventCountMap.values()) {
            eventCounts.add(eventCount);
        }
        return eventCounts;
    }

    @Override
    public Set<UserEvent> findSharedEventsWithFriend(UserData owner, UserData friend) {
        return userEventRepository.findSharedEventsWithFriend(owner, friend);
    }

    @Override
    public UserEvent save(UserEvent userEvent) {
        return userEventRepository.save(userEvent);
    }

    @Override
    public UserEvent findById(Long id) {
        return userEventRepository.findOne(id);
    }

    @Override
    public Set<UserEvent> findPublicEventsOfFriendsInTheFuture(UserData userData) {
        return userEventRepository.findPublicEventsOfFriendsInTheFuture(userData);
    }

    @Override
    public Set<UserEvent> findPublicEventsOfFriendsInTheFutureFetchMembers(UserData userData) {
        return userEventRepository.findPublicEventsOfFriendsInTheFutureFetchMembers(userData);
    }
}
