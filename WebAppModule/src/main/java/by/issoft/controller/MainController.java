package by.issoft.controller;

import by.issoft.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping
public class MainController {

    private final SessionManager sessionManager;

    @Autowired
    public MainController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLogin() {
        if (sessionManager.getAuthenticatedUser() != null) {
            return "main";
        }
        return "login";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String getMain() {
        return "main";
    }

}
