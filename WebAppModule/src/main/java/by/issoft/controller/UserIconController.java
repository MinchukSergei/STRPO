package by.issoft.controller;


import by.issoft.entity.UserData;
import by.issoft.entity.UserImage;
import by.issoft.entity.UserImageBlob;
import by.issoft.service.UserDataService;
import by.issoft.service.UserImageBlobService;
import by.issoft.util.MultiPartFileValidator;
import by.issoft.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/icon")
public class UserIconController {
    private final UserImageBlobService userImageBlobService;

    private final MultiPartFileValidator multiPartFileValidator;

    private final SessionManager sessionManager;

    private final UserDataService userDataService;

    @Autowired
    public UserIconController(UserImageBlobService userImageBlobService, MultiPartFileValidator multiPartFileValidator, SessionManager sessionManager, UserDataService userDataService) {
        this.userImageBlobService = userImageBlobService;
        this.multiPartFileValidator = multiPartFileValidator;
        this.sessionManager = sessionManager;
        this.userDataService = userDataService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity addIcon(@RequestPart("image") MultipartFile image) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;

        UserImageBlob userImageBlob = new UserImageBlob();
        status = multiPartFileValidator.validateImage(image);

        if (status.equals(HttpStatus.OK)) {
            try {
                userImageBlob.setImageBlob(image.getBytes());
            } catch (IOException ignored) {
            }
            userImageBlob = userImageBlobService.saveWithRandomName(userImageBlob);
            UserImage userImage = new UserImage();
            userImage.setId(userImageBlob.getId());
            currentUser.setUserIcon(userImage);
            userDataService.save(currentUser);
        }
        return new ResponseEntity(status);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity editIconByName(@RequestPart("image") MultipartFile image) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;

        UserImage currentImage = currentUser.getUserIcon();
        UserImageBlob userImageBlob;
        if (currentImage == null || currentImage.getImageName().equals(UserImage.DEFAULT_ICON_NAME)) {
            userImageBlob = new UserImageBlob();
        } else {
            userImageBlob = userImageBlobService.findByImageName(currentImage.getImageName());
        }

        if (userImageBlob == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            status = multiPartFileValidator.validateImage(image);
            if (status.equals(HttpStatus.OK)) {
                try {
                    userImageBlob.setImageBlob(image.getBytes());
                } catch (IOException ignored) {
                }
                UserImageBlob savedBlob = userImageBlobService.saveWithRandomName(userImageBlob);

                UserImage newImage = new UserImage();
                newImage.setId(savedBlob.getId());
                newImage.setImageName(savedBlob.getImageName());

                currentUser.setUserIcon(newImage);
                userDataService.save(currentUser);
            }
        }

        return new ResponseEntity<>(status);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteUserIcon() {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;

        UserImage currentImage = currentUser.getUserIcon();

        if (currentImage == null || currentImage.getImageName().equals(UserImage.DEFAULT_ICON_NAME)) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            UserImageBlob userImageBlob = userImageBlobService.findByImageName(currentImage.getImageName());
            userImageBlobService.delete(userImageBlob);
            status = HttpStatus.OK;
        }
        return new ResponseEntity(status);
    }
}
