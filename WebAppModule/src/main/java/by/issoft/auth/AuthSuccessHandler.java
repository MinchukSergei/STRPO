package by.issoft.auth;


import by.issoft.entity.UserData;
import by.issoft.service.LearningSetService;
import by.issoft.session.SessionManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private final LearningSetService learningSetService;
    private final SessionManager sessionManager;

    public AuthSuccessHandler(LearningSetService learningSetService, SessionManager sessionManager) {
        this.learningSetService = learningSetService;
        this.sessionManager = sessionManager;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        learningSetService.learnNeuralNetwork(currentUser);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }
}
