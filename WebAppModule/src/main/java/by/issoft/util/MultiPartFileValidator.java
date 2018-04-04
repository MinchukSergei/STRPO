package by.issoft.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultiPartFileValidator {

    public HttpStatus validateImage(MultipartFile image) {
        HttpStatus status;
        if (image.getSize() == 0) {
            status = HttpStatus.BAD_REQUEST;
        } else if (!isImageContentType(image)) {
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        } else {
            try {
                image.getBytes();
                status = HttpStatus.OK;
            } catch (IOException e) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return status;
    }

    private boolean isImageContentType(MultipartFile file) {
        return file.getContentType().equals(MediaType.IMAGE_GIF_VALUE) ||
                file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE) ||
                file.getContentType().equals(MediaType.IMAGE_PNG_VALUE);
    }
}
