package by.issoft.service;

import by.issoft.entity.UserData;
import by.issoft.entity.UserEvent;
import by.issoft.service.util.EventCount;

import java.util.List;
import java.util.Set;

public interface UserEventService {
    UserEvent findByIdAndFetchMembers(Long id);
    Set<UserEvent> findSharedEventsBetweenDatesByUserId(String dateFrom, String dateTo, String format, UserData user);
    List<EventCount> findCountSharedEventsBetweenDatesByUserId(String dateFrom, String dateTo, String format, UserData user);
    Set<UserEvent> findSharedEventsWithFriend(UserData owner, UserData friend);
    UserEvent save(UserEvent userEvent);
    UserEvent findById(Long id);
    Set<UserEvent> findPublicEventsOfFriendsInTheFuture(UserData userData);
    Set<UserEvent> findPublicEventsOfFriendsInTheFutureFetchMembers(UserData userData);
}
