package by.issoft.controller;


import by.issoft.entity.UserData;
import by.issoft.service.UserDataService;
import by.issoft.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/friend")
public class FriendListController {

    private final UserDataService userDataService;

    private final SessionManager sessionManager;


    @Autowired
    public FriendListController(UserDataService userDataService, SessionManager sessionManager) {
        this.userDataService = userDataService;
        this.sessionManager = sessionManager;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<UserData>> getFriendList() {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        Set<UserData> friends = new HashSet<>();

        UserData fetchedUser = userDataService.findByIdAndFetchFriendsList(currentUser.getId());
        if (fetchedUser != null) {
            friends = fetchedUser.getFriends();
        }
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.POST)
    public ResponseEntity addFriendByLogin(@PathVariable("login") String login) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;
        Set<UserData> friends = new HashSet<>();

        UserData friend = userDataService.findUserDataByLogin(login);
        if (friend == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            UserData fetchedUser = userDataService.findByIdAndFetchFriendsList(currentUser.getId());
            if (fetchedUser != null) {
                friends = fetchedUser.getFriends();
            }
            if (friends.contains(friend) || currentUser.equals(friend)) {
                status = HttpStatus.BAD_REQUEST;
            } else {
                friends.add(friend);
                currentUser.setFriends(friends);
                userDataService.save(currentUser);

                Set<UserData> friendsOfAFriend = new HashSet<>();
                UserData friendWithFriends = userDataService.findByIdAndFetchFriendsList(friend.getId());
                if (friendWithFriends != null) {
                    friendsOfAFriend = friendWithFriends.getFriends();
                }

                friendsOfAFriend.add(currentUser);
                friend.setFriends(friendsOfAFriend);
                userDataService.save(friend);

                status = HttpStatus.OK;
            }
        }

        return new ResponseEntity(status);
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFriendByLogin(@PathVariable("login") String login) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;
        Set<UserData> friends = new HashSet<>();

        UserData friend = userDataService.findUserDataByLogin(login);
        if (friend == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            UserData fetchedUser = userDataService.findByIdAndFetchFriendsList(currentUser.getId());
            if (fetchedUser != null) {
                friends = fetchedUser.getFriends();
            }
            friends.remove(friend);
            currentUser.setFriends(friends);
            userDataService.save(currentUser);

            Set<UserData> friendsOfAFriend = new HashSet<>();
            UserData friendWithFriends = userDataService.findByIdAndFetchFriendsList(friend.getId());
            if (friendWithFriends != null) {
                friendsOfAFriend = friendWithFriends.getFriends();
            }

            friendsOfAFriend.remove(currentUser);
            friend.setFriends(friendsOfAFriend);
            userDataService.save(friend);

            status = HttpStatus.OK;
        }

        return new ResponseEntity(status);
    }
}
