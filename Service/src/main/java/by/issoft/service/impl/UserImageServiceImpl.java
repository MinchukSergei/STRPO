package by.issoft.service.impl;


import by.issoft.entity.UserImage;
import by.issoft.repository.UserImageRepository;
import by.issoft.service.UserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserImageServiceImpl implements UserImageService {


    private final UserImageRepository userImageRepository;

    @Autowired
    public UserImageServiceImpl(UserImageRepository userImageRepository) {
        this.userImageRepository = userImageRepository;
    }

    @Override
    public UserImage findByImageName(String name) {
        return userImageRepository.findByImageName(name);
    }

    @Override
    public UserImage save(UserImage image) {
        return userImageRepository.save(image);
    }

    @Override
    public void delete(UserImage image) {
        userImageRepository.delete(image);
    }
}
