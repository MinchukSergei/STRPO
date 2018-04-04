package by.issoft.controller;


import by.issoft.entity.UserData;
import by.issoft.entity.UserImage;
import by.issoft.service.UserDataService;
import by.issoft.service.UserImageService;
import by.issoft.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserDataController {
    private final UserDataService userDataService;

    private final PasswordEncoder passwordEncoder;

    private final SessionManager sessionManager;

    private final UserImageService userImageService;

    @Autowired
    public UserDataController(UserDataService userDataService,
                              PasswordEncoder passwordEncoder,
                              SessionManager sessionManager,
                              UserImageService userImageService) {
        this.userDataService = userDataService;
        this.passwordEncoder = passwordEncoder;
        this.sessionManager = sessionManager;
        this.userImageService = userImageService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<UserData> getCurrentUser() {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody UserData userData) {
        UserData exists = userDataService.findUserDataByLogin(userData.getUserLogin());
        HttpStatus result;
        if (exists == null) {
            userData.setUserPassword(passwordEncoder.encode(userData.getUserPassword()));
            userDataService.save(userData);
            result = HttpStatus.OK;
        } else {
            result = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity(result);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity editUser(@RequestBody UserData userData) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;

        if (userData.getUserPassword() != null) {
            currentUser.setUserPassword(passwordEncoder.encode(userData.getUserPassword()));
        }
        currentUser.setUserAddress(userData.getUserAddress());
        currentUser.setUserEmail(userData.getUserEmail());
        currentUser.setUserFirstName(userData.getUserFirstName());
        currentUser.setUserLastName(userData.getUserLastName());
        currentUser.setUserPhone(userData.getUserPhone());

        userDataService.save(currentUser);
        status = HttpStatus.OK;
        return new ResponseEntity(status);
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<UserData> getUserByLogin(@PathVariable String login) {
        UserData userData = userDataService.findUserDataByLogin(login);
        HttpStatus status;

        if (userData == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(userData, status);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteUser() {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;

        UserImage image = currentUser.getUserIcon();
        if (image != null) {
            userImageService.delete(image);
            currentUser.setUserIcon(null);
        }
        userDataService.delete(currentUser);
        status = HttpStatus.OK;
        return new ResponseEntity(status);
    }


}
