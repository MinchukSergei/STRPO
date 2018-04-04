package by.issoft.controller;

import by.issoft.entity.UserData;
import by.issoft.entity.UserImage;
import by.issoft.entity.UserImageBlob;
import by.issoft.service.UserDataService;
import by.issoft.service.UserImageBlobService;
import by.issoft.service.UserImageService;
import by.issoft.session.SessionManager;
import by.issoft.util.MultiPartFileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/image")
public class UserImageController {
    private final UserImageBlobService userImageBlobService;

    private final UserDataService userDataService;

    private final SessionManager sessionManager;

    private final MultiPartFileValidator multiPartFileValidator;

    private final UserImageService userImageService;

    @Autowired
    public UserImageController(UserImageBlobService userImageBlobService, SessionManager sessionManager, UserDataService userDataService, MultiPartFileValidator multiPartFileValidator, UserImageService userImageService) {
        this.userImageBlobService = userImageBlobService;
        this.sessionManager = sessionManager;
        this.userDataService = userDataService;
        this.multiPartFileValidator = multiPartFileValidator;
        this.userImageService = userImageService;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces =
            {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> getImageByName(@PathVariable String name) {
        UserImageBlob userImageBlob = userImageBlobService.findByImageName(name);
        byte[] imageBytes = null;
        HttpStatus status;

        if (userImageBlob != null) {
            imageBytes = userImageBlob.getImageBlob();
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(imageBytes, status);
    }

    @RequestMapping(value = "/gallery/{login}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<UserImage>> getGalleryByUserLogin(@PathVariable String login) {
        UserData userData = userDataService.findUserDataByLogin(login);
        HttpStatus status;
        Set<UserImage> gallery = new HashSet<>();

        if (userData == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            UserData fetchedUser = userDataService.findByIdAndFetchGallery(userData.getId());
            if (fetchedUser != null) {
                gallery = fetchedUser.getGallery();
            }
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(gallery, status);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity addImageToGallery(@RequestPart("image") MultipartFile image) {
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

            Set<UserImage> gallery = new HashSet<>();
            UserData fetchedUser = userDataService.findByIdAndFetchGallery(currentUser.getId());
            if (fetchedUser != null) {
                gallery = fetchedUser.getGallery();
            }
            gallery.add(userImage);
            currentUser.setGallery(gallery);
            userDataService.save(currentUser);
        }

        return new ResponseEntity(status);
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteImageFromGalleryByName(@PathVariable String name) {
        UserData currentUser = sessionManager.getAuthenticatedUser();
        HttpStatus status;

        Set<UserImage> gallery = new HashSet<>();
        UserData fetchedUser = userDataService.findByIdAndFetchGallery(currentUser.getId());
        if (fetchedUser != null) {
            gallery = fetchedUser.getGallery();
        }
        UserImage userImage = userImageService.findByImageName(name);
        if (userImage == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            if (gallery.contains(userImage)) {
                userImageService.delete(userImage);
                status = HttpStatus.OK;
            } else {
                status = HttpStatus.NOT_FOUND;
            }
        }

        return new ResponseEntity(status);
    }

}













