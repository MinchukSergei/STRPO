package by.issoft.service.impl;


import by.issoft.entity.UserData;
import by.issoft.repository.UserDataRepository;
import by.issoft.service.UserDataService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDataServiceImpl implements UserDataService {
    private final UserDataRepository userDataRepository;

    @Autowired
    public UserDataServiceImpl(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public List<UserData> findAll() {
        return Lists.newArrayList(userDataRepository.findAll());
    }

    public UserData findUserDataByLogin(String login) {
        return userDataRepository.findUserDataByUserLogin(login);
    }

    @Override
    public UserData save(UserData userData) {
        return userDataRepository.save(userData);
    }

    @Override
    public void delete(UserData userData) {
        userDataRepository.delete(userData);
    }

    @Override
    public UserData findByIdAndFetchFriendsList(Long id) {
        return userDataRepository.findByIdAndFetchFriendsList(id);
    }

    @Override
    public UserData findByIdAndFetchGallery(Long id) {
        return userDataRepository.findByIdAndFetchGallery(id);
    }

    @Override
    public UserData findByIdAndFetchOwnEvents(Long id) {
        return userDataRepository.findByIdAndFetchOwnEvents(id);
    }


}
