package by.issoft.repository;


import by.issoft.entity.MessageList;
import by.issoft.entity.UserData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MessageListRepository extends CrudRepository<MessageList, Long> {

    @Query("SELECT ml FROM MessageList ml " +
            "WHERE" +
            "(:currentUser <> :user AND " +
            "((ml.messageReceiver = :currentUser OR ml.messageSender = :currentUser) " +
            "       AND (ml.messageReceiver = :user OR ml.messageSender = :user)))" +
            "OR " +
            "(:currentUser = :user AND " +
            "(ml.messageReceiver = :currentUser AND ml.messageSender = :currentUser)) " +
            "ORDER BY ml.messageTime")
    Set<MessageList> getAllMessagesFromUser(@Param("currentUser") UserData currentUser, @Param("user") UserData user);
}
