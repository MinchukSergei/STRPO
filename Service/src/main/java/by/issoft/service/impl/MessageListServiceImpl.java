package by.issoft.service.impl;

import by.issoft.entity.MessageList;
import by.issoft.entity.UserData;
import by.issoft.repository.MessageListRepository;
import by.issoft.service.MessageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MessageListServiceImpl implements MessageListService {
    private final MessageListRepository messageListRepository;

    @Autowired
    public MessageListServiceImpl(MessageListRepository messageListRepository) {
        this.messageListRepository = messageListRepository;
    }

    @Override
    public Set<MessageList> getAllMessagesFromUser(UserData currentUser, UserData user) {
        return messageListRepository.getAllMessagesFromUser(currentUser, user);
    }

    @Override
    public MessageList save(MessageList messageList) {
        return messageListRepository.save(messageList);
    }

    @Override
    public MessageList findOne(MessageList messageList) {
        return messageListRepository.findOne(messageList.getId());
    }
}
