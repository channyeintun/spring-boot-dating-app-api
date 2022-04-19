package com.pledge.app.util;

import com.pledge.app.exception.CustomErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

import static org.apache.http.entity.ContentType.*;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;

public class FileValidator {
    public static void validateFile(MultipartFile file) {
        if (file.isEmpty()) throw new CustomErrorException(HttpStatus.BAD_REQUEST,"Cannot upload empty file");
        //Check if the file is an image
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST,"FIle uploaded is not an image");
        }
    }
}
