package by.issoft.session;


import by.issoft.entity.UserData;
import by.issoft.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionManager {
    private final UserDataService userDataService;

    @Autowired
    public SessionManager(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    public UserData getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserData userData = null;
        User user;
        if (authentication.getPrincipal() instanceof User) {
            user = (User) authentication.getPrincipal();
            userData = userDataService.findUserDataByLogin(user.getUsername());
        }
        return userData;
    }
}
