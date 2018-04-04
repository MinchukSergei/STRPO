package by.issoft.controller;


import by.issoft.entity.EventMemberList;
import by.issoft.entity.EventType;
import by.issoft.entity.UserData;
import by.issoft.entity.UserEvent;
import by.issoft.service.EventMemberListService;
import by.issoft.service.EventTypeService;
import by.issoft.service.UserDataService;
import by.issoft.service.UserEventService;
import by.issoft.service.util.EventCount;
import by.issoft.session.SessionManager;
import by.issoft.util.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/event")
public class UserEventController {
    private static final String DATE_FORMAT = "%d-%m-%Y";

    private final SessionManager sessionManager;
    private final UserDataService userDataService;
    private final UserEventService userEventService;
    private final EventTypeService eventTypeService;
    private final DateValidator dateValidator;
    private final EventMemberListService eventMemberListService;

    @Autowired
    public UserEventController(SessionManager sessionManager,
                               UserDataService userDataService,
                               UserEventService userEventService,
                               EventTypeService eventTypeService, DateValidator dateValidator, EventMemberListService eventMemberListService) {
        this.sessionManager = sessionManager;
        this.userDataService = userDataService;
        this.userEventService = userEventService;
        this.eventTypeService = eventTypeService;
        this.dateValidator = dateValidator;
        this.eventMemberListService = eventMemberListService;
    }

    @RequestMapping(value = "/{from}/{to}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<UserEvent>> getEventsBetweenDates(@PathVariable("from") String from,
                                                                @PathVariable("to") String to) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;
        Set<UserEvent> sharedEvents = new HashSet<>();

        if (!dateValidator.dateIsValid(from) || !dateValidator.dateIsValid(to)) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            sharedEvents = userEventService.findSharedEventsBetweenDatesByUserId(from, to, DATE_FORMAT, currentUser);
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(sharedEvents, status);
    }



    @RequestMapping(value = "/count/{from}/{to}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<EventCount>> getEventCountBetweenDates(@PathVariable("from") String from,
                                                                      @PathVariable("to") String to) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;
        List<EventCount> countSharedEvents = new ArrayList<>();

        if (!dateValidator.dateIsValid(from) || !dateValidator.dateIsValid(to)) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            countSharedEvents = userEventService.findCountSharedEventsBetweenDatesByUserId(from, to, DATE_FORMAT, currentUser);
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(countSharedEvents, status);
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<UserEvent>> getEventsSharedWithUser(@PathVariable("login") String login) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;
        Set<UserEvent> events = new HashSet<>();

        UserData friend = userDataService.findUserDataByLogin(login);
        if (friend == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            UserData fetchedUser = userDataService.findByIdAndFetchFriendsList(currentUser.getId());
            Set<UserData> friends = new HashSet<>();
            if (fetchedUser != null) {
                friends = fetchedUser.getFriends();
            }
            if (friends.contains(friend)) {
                events = userEventService.findSharedEventsWithFriend(currentUser, friend);
                status = HttpStatus.OK;
            } else {
                status = HttpStatus.FORBIDDEN;
            }
        }
        return new ResponseEntity<>(events, status);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserEvent>> getOwnEvents() {
        UserData currentUser = sessionManager.getAuthenticatedUser();

        UserData fetchedWithOwnEventsUser = userDataService.findByIdAndFetchOwnEvents(currentUser.getId());
        Set<UserEvent> ownEvents = new HashSet<>();
        if (fetchedWithOwnEventsUser != null) {
            ownEvents = fetchedWithOwnEventsUser.getOwnEvents();
        }
        List<UserEvent> events = new ArrayList<>(ownEvents);
        events.sort(Comparator.comparing(UserEvent::getEventTimestamp));

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UserEvent> addEvent(@RequestBody UserEvent userEvent) {
        UserData currentUser = sessionManager.getAuthenticatedUser();

        EventType eventType = eventTypeService.findByTypeName(userEvent.getEventName());
        if (eventType == null) {
            eventType = eventTypeService.findByTypeName(EventType.DEFAULT_TYPE);
        }

        userEvent.setUserOwner(currentUser);
        userEvent.setEventType(eventType);
        UserEvent savedEvent = userEventService.save(userEvent);
        return new ResponseEntity<>(savedEvent, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity editEvent(@RequestBody UserEvent userEvent) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;

        UserData fetchedWithOwnEventsUser = userDataService.findByIdAndFetchOwnEvents(currentUser.getId());
        Set<UserEvent> ownEvents = new HashSet<>();
        if (fetchedWithOwnEventsUser != null) {
            ownEvents = fetchedWithOwnEventsUser.getOwnEvents();
        }

        UserEvent oldEvent = userEventService.findById(userEvent.getId());
        if (oldEvent == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            if (ownEvents.contains(oldEvent)) {
                ownEvents.remove(oldEvent);
                EventType eventType = eventTypeService.findByTypeName(userEvent.getEventType().getTypeName());
                if (eventType != null) {
                    oldEvent.setEventType(eventType);
                }
                oldEvent.setEventName(userEvent.getEventName());
                oldEvent.setEventTimestamp(userEvent.getEventTimestamp());
                oldEvent.setPublic(userEvent.getPublic());
                ownEvents.add(oldEvent);
                currentUser.setOwnEvents(ownEvents);
                userDataService.save(currentUser);
                status = HttpStatus.OK;
            } else {
                status = HttpStatus.FORBIDDEN;
            }
        }
        return new ResponseEntity(status);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteEvent(@PathVariable("id") Long eventId) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;

        UserData fetchedWithOwnEventsUser = userDataService.findByIdAndFetchOwnEvents(currentUser.getId());
        Set<UserEvent> ownEvents = new HashSet<>();
        if (fetchedWithOwnEventsUser != null) {
            ownEvents = fetchedWithOwnEventsUser.getOwnEvents();
        }

        UserEvent userEvent = userEventService.findById(eventId);
        if (userEvent == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            if (ownEvents.contains(userEvent)) {
                ownEvents.remove(userEvent);
                currentUser.setOwnEvents(ownEvents);
                userDataService.save(currentUser);
                status = HttpStatus.OK;
            } else {
                EventMemberList eventMemberList = eventMemberListService.findByEventAndMember(userEvent, currentUser);
                if (eventMemberList != null) {
                    eventMemberListService.delete(eventMemberList);
                    status = HttpStatus.OK;
                } else {
                    status = HttpStatus.FORBIDDEN;
                }
            }
        }

        return new ResponseEntity(status);
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ResponseEntity generateRandomEvents() {
        UserData currentUser = sessionManager.getAuthenticatedUser();

        UserData fetchedWithFriendsCurrentUser = userDataService.findByIdAndFetchFriendsList(currentUser.getId());
        Set<UserData> friends;
        if (fetchedWithFriendsCurrentUser == null) {
            friends = new HashSet<>();
        } else {
            friends = fetchedWithFriendsCurrentUser.getFriends();
        }

        List<EventType> eventTypes = new ArrayList<>(eventTypeService.findAll());

        Random rnd = new Random();

        for (UserData user : friends) {
            int eventCount = 7 + rnd.nextInt(7);

            UserData fetchedWithFriendsFriend = userDataService.findByIdAndFetchFriendsList(user.getId());
            Set<UserData> friendsOfFriend;
            if (fetchedWithFriendsCurrentUser == null) {
                friendsOfFriend = new HashSet<>();
            } else {
                friendsOfFriend = fetchedWithFriendsFriend.getFriends();
            }

            for (int i = 0; i < eventCount; i ++) {
                UserEvent userEvent = new UserEvent();
                userEvent.setUserOwner(user);
                userEvent.setPublic(true);

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 1 + rnd.nextInt(60));
                calendar.set(Calendar.HOUR_OF_DAY, 9 + rnd.nextInt(15));
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                userEvent.setEventTimestamp(calendar);

                EventType randomEventType = eventTypes.get(rnd.nextInt(eventTypes.size()));
                userEvent.setEventType(randomEventType);

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String formatted = format.format(calendar.getTime());

                Set<UserData> eventParticipants = new HashSet<>();
                for (UserData friendOfFriend : friendsOfFriend) {
                    boolean isParticipant = rnd.nextBoolean();

                    if (isParticipant && !friendOfFriend.equals(currentUser)) {
                        eventParticipants.add(friendOfFriend);
                    }
                }
                userEvent.setEventMembers(eventParticipants);
                userEvent.setEventName(formatted + " " + user.getUserFirstName() + " " + randomEventType.getTypeName());
                userEventService.save(userEvent);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
