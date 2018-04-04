package by.issoft.service;

import by.issoft.entity.UserImage;

public interface UserImageService {
    UserImage findByImageName(String name);
    UserImage save(UserImage image);
    void delete(UserImage image);
}
