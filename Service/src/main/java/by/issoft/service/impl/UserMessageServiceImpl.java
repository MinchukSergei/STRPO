package by.issoft.service.impl;

import by.issoft.entity.UserMessage;
import by.issoft.repository.UserMessageRepository;
import by.issoft.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    private final UserMessageRepository userMessageRepository;

    @Autowired
    public UserMessageServiceImpl(UserMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    public UserMessage save(UserMessage userMessage) {
        return userMessageRepository.save(userMessage);
    }
}
