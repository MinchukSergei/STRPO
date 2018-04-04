package by.issoft.controller;


import by.issoft.entity.MessageList;
import by.issoft.entity.UserData;
import by.issoft.entity.UserMessage;
import by.issoft.service.UserDataService;
import by.issoft.service.MessageListService;
import by.issoft.service.UserMessageService;
import by.issoft.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/message")
public class UserMessageController {
    private final int MESSAGE_MAX_SIZE = 65537;
    private final int MESSAGE_MIN_SIZE = 1;

    private final SessionManager sessionManager;
    private final MessageListService messageListService;
    private final UserDataService userDataService;
    private final UserMessageService userMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public UserMessageController(SessionManager sessionManager, MessageListService messageListService, UserDataService userDataService, UserMessageService userMessageService, SimpMessagingTemplate simpMessagingTemplate) {
        this.sessionManager = sessionManager;
        this.messageListService = messageListService;
        this.userDataService = userDataService;
        this.userMessageService = userMessageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<MessageList>> getMessages(@PathVariable String login) {
        UserData currentUser = sessionManager.getAuthenticatedUser();

        UserData userData = userDataService.findUserDataByLogin(login);
        HttpStatus status;

        Set<MessageList> messageLists = new HashSet<>();
        if (userData != null) {
            messageLists = messageListService.getAllMessagesFromUser(currentUser, userData);
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(messageLists, status);
    }

    @MessageMapping("/chat.private.{username}")
    public ResponseEntity postMessageToUser(@Payload String message, @DestinationVariable("username") String login, Principal principal) {
        UserData currentUser = userDataService.findUserDataByLogin(principal.getName());
        ResponseEntity responseEntity;
        HttpStatus status;
        message = message.trim();

        if (message.length() < MESSAGE_MIN_SIZE || message.length() > MESSAGE_MAX_SIZE) {
            status = HttpStatus.BAD_REQUEST;
            responseEntity = new ResponseEntity(status);
        } else {
            UserData userData = userDataService.findUserDataByLogin(login);
            if (userData != null) {
                status = HttpStatus.OK;
                UserMessage userMessage = new UserMessage();
                userMessage.setMessageText(message);

                MessageList messageList = new MessageList();
                messageList.setMessage(userMessage);
                messageList.setMessageSender(currentUser);
                messageList.setMessageReceiver(userData);

                messageListService.save(messageList);
                messageList = messageListService.findOne(messageList);
                responseEntity = new ResponseEntity<>(messageList, status);
                simpMessagingTemplate.convertAndSend("/user/" + currentUser.getUserLogin() + "/exchange/amq.direct/chat.message", messageList);
                simpMessagingTemplate.convertAndSend("/user/" + login + "/exchange/amq.direct/chat.message", messageList);
            } else {
                status = HttpStatus.NOT_FOUND;
                responseEntity = new ResponseEntity(status);
            }
        }

        return responseEntity;
    }
}
