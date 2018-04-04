package by.issoft.repository;

import by.issoft.entity.EventMemberList;
import by.issoft.entity.UserData;
import by.issoft.entity.UserEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EventMemberListRepository extends CrudRepository<EventMemberList, Long> {

    @Query("SELECT fr FROM UserData ud LEFT JOIN ud.friends fr WHERE fr IS NOT NULL AND ud = :user AND fr NOT IN " +
            "(SELECT em.member FROM EventMemberList em WHERE em.event = :event)")
    Set<UserData> findFriendsNotInEvent(@Param("user") UserData user, @Param("event") UserEvent event);

    EventMemberList findByEventAndMember(UserEvent event, UserData member);
}
