package by.issoft.repository;


import by.issoft.entity.UserData;
import by.issoft.entity.UserEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserEventRepository extends CrudRepository<UserEvent, Long> {

    @Query("SELECT ue FROM UserEvent ue JOIN FETCH ue.eventMembers WHERE ue.id = (:id)")
    UserEvent findByIdAndFetchMembers(@Param("id") Long id);

    @Query("SELECT ue FROM UserEvent ue LEFT JOIN ue.memberList em " +
            "WHERE (ue.userOwner = :user OR em.member = :user) " +
            "AND ue.eventTimestamp BETWEEN STR_TO_DATE(:dateFrom, :format) " +
            "AND STR_TO_DATE(:dateTo, :format)")
    Set<UserEvent> findSharedEventsBetweenDatesByUserId(@Param("dateFrom") String dateFrom,
                                                        @Param("dateTo") String dateTo,
                                                        @Param("format") String dateFormat,
                                                        @Param("user") UserData user);

    @Query("SELECT DISTINCT ue.id, DATE_FORMAT(ue.eventTimestamp, :selectFormat), ue.userOwner " +
            "FROM UserEvent ue LEFT JOIN ue.memberList em " +
            "WHERE (ue.userOwner = :user OR em.member = :user) " +
            "AND ue.eventTimestamp BETWEEN STR_TO_DATE(:dateFrom, :format) " +
            "AND STR_TO_DATE(:dateTo, :format)")
    Set<Object[]> findCountSharedEventsBetweenDatesByUserId(@Param("selectFormat") String selectFormat,
                                                            @Param("dateFrom") String dateFrom,
                                                            @Param("dateTo") String dateTo,
                                                            @Param("format") String dateFormat,
                                                            @Param("user") UserData user);

    @Query("SELECT ue FROM UserEvent ue LEFT JOIN ue.memberList em " +
            "WHERE (ue.userOwner IN (:owner, :friend) AND em.member IN (:owner, :friend) " +
            "OR (em.member = :owner AND em.event IN " +
            "(SELECT em2.event FROM EventMemberList em2 WHERE em2.member = :friend)))")
    Set<UserEvent> findSharedEventsWithFriend(@Param("owner") UserData owner, @Param("friend") UserData friend);

    @Query("SELECT ue FROM UserData ud " +
            "LEFT JOIN ud.friends fr " +
            "LEFT JOIN fr.ownEvents ue " +
            "WHERE ud = :user " +
                "AND ue.eventTimestamp > CURDATE() " +
                "AND ue.isPublic = TRUE " +
            "ORDER BY ue.eventTimestamp")
    Set<UserEvent> findPublicEventsOfFriendsInTheFuture(@Param("user") UserData userData);

    @Query("SELECT ue FROM UserData ud " +
            "LEFT JOIN ud.friends fr " +
            "LEFT JOIN fr.ownEvents ue " +
            "LEFT JOIN FETCH ue.eventMembers " +
            "WHERE ud = :user " +
            "AND ue.eventTimestamp > CURDATE() " +
            "AND ue.isPublic = TRUE " +
            "ORDER BY ue.eventTimestamp")
    Set<UserEvent> findPublicEventsOfFriendsInTheFutureFetchMembers(@Param("user") UserData userData);
}
