package by.issoft.service.impl;


import by.issoft.entity.UserImageBlob;
import by.issoft.repository.UserImageBlobRepository;
import by.issoft.service.UserImageBlobService;
import by.issoft.service.util.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserImageBlobServiceImpl implements UserImageBlobService {
    private final int IMAGE_NAME_LENGTH = 32;

    private final RandomString randomString;

    private final UserImageBlobRepository userImageBlobRepository;

    @Autowired
    public UserImageBlobServiceImpl(RandomString randomString, UserImageBlobRepository userImageBlobRepository) {
        this.randomString = randomString;
        this.userImageBlobRepository = userImageBlobRepository;
    }

    @Override
    public UserImageBlob saveWithRandomName(UserImageBlob image) {
        String randomName;
        UserImageBlob userImageBlob;

        while (true) {
            randomName = randomString.getRandomString(IMAGE_NAME_LENGTH);
            userImageBlob = userImageBlobRepository.findByImageName(randomName);
            if (userImageBlob == null) {
                image.setImageName(randomName);
                userImageBlob = userImageBlobRepository.save(image);
                break;
            }
        }

        return userImageBlob;
    }

    @Override
    public UserImageBlob findByImageName(String name) {
        return userImageBlobRepository.findByImageName(name);
    }

    @Override
    public void delete(UserImageBlob userImageBlob) {
        userImageBlobRepository.delete(userImageBlob);
    }
}
