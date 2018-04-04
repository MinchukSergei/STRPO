package by.issoft.controller;

import by.issoft.entity.UserData;
import by.issoft.entity.UserEvent;
import by.issoft.service.EventMemberListService;
import by.issoft.service.UserDataService;
import by.issoft.service.UserEventService;
import by.issoft.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/member")
public class EventMemberListController {

    private final SessionManager sessionManager;
    private final UserDataService userDataService;
    private final UserEventService userEventService;
    private final EventMemberListService eventMemberListService;

    @Autowired
    public EventMemberListController(SessionManager sessionManager, UserDataService userDataService, UserEventService userEventService, EventMemberListService eventMemberListService) {
        this.sessionManager = sessionManager;
        this.userDataService = userDataService;
        this.userEventService = userEventService;
        this.eventMemberListService = eventMemberListService;
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<UserData>> getMembersFromEvent(@PathVariable Long eventId) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;
        Set<UserData> members = new HashSet<>();

        UserEvent userEvent = userEventService.findById(eventId);
        if (userEvent == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            UserEvent fetchedUserEventWithMembers = userEventService.findByIdAndFetchMembers(userEvent.getId());

            if (fetchedUserEventWithMembers != null) {
                members = fetchedUserEventWithMembers.getEventMembers();
            }

            UserData fetchedWithFriendsEventOwner = userDataService.findByIdAndFetchFriendsList(userEvent.getUserOwner().getId());
            Set<UserData> friendsOfTheOwner;

            if (fetchedWithFriendsEventOwner == null) {
                friendsOfTheOwner = new HashSet<>();
            } else {
                friendsOfTheOwner = fetchedWithFriendsEventOwner.getFriends();
            }
            if (friendsOfTheOwner.contains(currentUser) && userEvent.getPublic()) {
                status = HttpStatus.OK;
            } else {
                members.clear();
                status = HttpStatus.FORBIDDEN;
            }
        }

        return new ResponseEntity<>(members, status);
    }

    @RequestMapping(value = "/not/{eventId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<UserData>> getFriendsNotInEvent(@PathVariable Long eventId) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;
        Set<UserData> friendsNotInEvent = new HashSet<>();

        UserEvent userEvent = userEventService.findById(eventId);
        if (userEvent == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            UserData fetchedUserWithOwnEvents = userDataService.findByIdAndFetchOwnEvents(currentUser.getId());
            Set<UserEvent> ownEvents = new HashSet<>();

            if (fetchedUserWithOwnEvents != null) {
                ownEvents = fetchedUserWithOwnEvents.getOwnEvents();
            }

            if (!ownEvents.contains(userEvent)) {
                status = HttpStatus.FORBIDDEN;
            } else {
                friendsNotInEvent = eventMemberListService.findFriendsNotInEvent(currentUser, userEvent);
                status = HttpStatus.OK;
            }
        }

        return new ResponseEntity<>(friendsNotInEvent, status);
    }

    @RequestMapping(value = "/{eventId}/{friendLogin}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity addMemberToEvent(@PathVariable Long eventId, @PathVariable String friendLogin) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;
        Set<UserEvent> ownEvents = new HashSet<>();

        UserData fetchedUserWithEvents = userDataService.findByIdAndFetchOwnEvents(currentUser.getId());
        if (fetchedUserWithEvents != null) {
            ownEvents = fetchedUserWithEvents.getOwnEvents();
        }

        UserEvent userEvent = userEventService.findById(eventId);
        if (userEvent == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            if (ownEvents.contains(userEvent)) {
                UserData friend = userDataService.findUserDataByLogin(friendLogin);
                if (friend == null) {
                    status = HttpStatus.NOT_FOUND;
                } else {
                    UserEvent fetchedUserEventWithMembers = userEventService.findByIdAndFetchMembers(userEvent.getId());
                    Set<UserData> members = new HashSet<>();

                    if (fetchedUserEventWithMembers != null) {
                        members = fetchedUserEventWithMembers.getEventMembers();
                    }

                    if (members.contains(friend)) {
                        status = HttpStatus.BAD_REQUEST;
                    } else {
                        members.add(friend);
                        userEvent.setEventMembers(members);
                        userEventService.save(userEvent);
                        status = HttpStatus.OK;
                    }
                }

            } else {
                status = HttpStatus.FORBIDDEN;
            }
        }

        return new ResponseEntity(status);
    }

    @RequestMapping(value = "/{eventId}/{friendLogin}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteMemberFromEvent(@PathVariable Long eventId, @PathVariable String friendLogin) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;

        Set<UserEvent> ownEvents = new HashSet<>();

        UserData fetchedUserWithEvents = userDataService.findByIdAndFetchOwnEvents(currentUser.getId());
        if (fetchedUserWithEvents != null) {
            ownEvents = fetchedUserWithEvents.getOwnEvents();
        }

        UserEvent userEvent = userEventService.findById(eventId);
        if (userEvent == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            if (ownEvents.contains(userEvent)) {
                UserData friend = userDataService.findUserDataByLogin(friendLogin);
                if (friend == null) {
                    status = HttpStatus.NOT_FOUND;
                } else {
                    UserEvent fetchedUserEventWithMembers = userEventService.findByIdAndFetchMembers(userEvent.getId());
                    Set<UserData> members = new HashSet<>();

                    if (fetchedUserEventWithMembers != null) {
                        members = fetchedUserEventWithMembers.getEventMembers();
                    }

                    if (members.contains(friend)) {
                        members.remove(friend);
                        userEvent.setEventMembers(members);
                        userEventService.save(userEvent);
                        status = HttpStatus.OK;
                    } else {
                        status = HttpStatus.BAD_REQUEST;
                    }
                }

            } else {
                status = HttpStatus.FORBIDDEN;
            }
        }

        return new ResponseEntity(status);
    }
}
