package by.issoft.service;

import by.issoft.entity.UserData;

import java.util.List;

public interface UserDataService {
    List<UserData> findAll();
    UserData findUserDataByLogin(String login);
    UserData save(UserData userData);
    void delete(UserData userData);
    UserData findByIdAndFetchFriendsList(Long id);
    UserData findByIdAndFetchGallery(Long id);
    UserData findByIdAndFetchOwnEvents(Long id);
}
