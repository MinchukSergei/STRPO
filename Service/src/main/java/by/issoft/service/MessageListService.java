package by.issoft.service;

import by.issoft.entity.MessageList;
import by.issoft.entity.UserData;

import java.util.Set;

public interface MessageListService {
    Set<MessageList> getAllMessagesFromUser(UserData currentUser, UserData user);
    MessageList save(MessageList messageList);
    MessageList findOne(MessageList messageList);
}
