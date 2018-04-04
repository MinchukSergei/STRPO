package by.issoft.repository;

import by.issoft.entity.UserData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends CrudRepository<UserData, Long> {
    UserData findUserDataByUserLogin(String login);

    @Query("SELECT ud FROM UserData ud JOIN FETCH ud.friends WHERE ud.id = (:id)")
    UserData findByIdAndFetchFriendsList(@Param("id") Long id);

    @Query("SELECT ud FROM UserData ud JOIN FETCH ud.gallery WHERE ud.id = (:id)")
    UserData findByIdAndFetchGallery(@Param("id") Long id);

    @Query("SELECT ud FROM UserData ud JOIN FETCH ud.ownEvents WHERE ud.id = (:id)")
    UserData findByIdAndFetchOwnEvents(@Param("id") Long id);
}
