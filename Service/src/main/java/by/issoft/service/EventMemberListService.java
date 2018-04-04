package by.issoft.service;


import by.issoft.entity.EventMemberList;
import by.issoft.entity.UserData;
import by.issoft.entity.UserEvent;

import java.util.Set;

public interface EventMemberListService {
    Set<UserData> findFriendsNotInEvent(UserData user, UserEvent event);
    EventMemberList findByEventAndMember(UserEvent event, UserData member);
    void delete(EventMemberList eventMemberList);
}
