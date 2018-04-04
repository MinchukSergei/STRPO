package by.issoft.service.impl;

import by.issoft.entity.EventMemberList;
import by.issoft.entity.UserData;
import by.issoft.entity.UserEvent;
import by.issoft.repository.EventMemberListRepository;
import by.issoft.service.EventMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EventMemberListServiceImpl implements EventMemberListService {

    private final EventMemberListRepository eventMemberListRepository;

    @Autowired
    public EventMemberListServiceImpl(EventMemberListRepository eventMemberListRepository) {
        this.eventMemberListRepository = eventMemberListRepository;
    }

    @Override
    public Set<UserData> findFriendsNotInEvent(UserData user, UserEvent event) {
        return eventMemberListRepository.findFriendsNotInEvent(user, event);
    }

    @Override
    public EventMemberList findByEventAndMember(UserEvent event, UserData member) {
        return eventMemberListRepository.findByEventAndMember(event, member);
    }

    @Override
    public void delete(EventMemberList eventMemberList) {
        eventMemberListRepository.delete(eventMemberList);
    }
}
