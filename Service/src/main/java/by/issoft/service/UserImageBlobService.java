package by.issoft.service;

import by.issoft.entity.UserImageBlob;

public interface UserImageBlobService {
    UserImageBlob saveWithRandomName(UserImageBlob userImageBlob);
    UserImageBlob findByImageName(String name);
    void delete(UserImageBlob userImageBlob);

}
