package com.pledge.app.service.impl;

import com.pledge.app.config.s3.Bucket;
import com.pledge.app.dao.ProfilePictureDAO;
import com.pledge.app.entity.ProfilePicture;
import com.pledge.app.entity.User;
import com.pledge.app.exception.CustomErrorException;
import com.pledge.app.service.FileStore;
import com.pledge.app.service.ProfilePictureService;
import com.pledge.app.service.UserService;
import com.pledge.app.util.FileValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ProfilePictureServiceImpl implements ProfilePictureService {

    @Autowired
    FileStore fileStore;
    @Autowired
    ProfilePictureDAO profilePictureDAO;
    @Autowired
    UserService userService;

    @Override
    public ProfilePicture save(MultipartFile file, String username) {
        Optional<User> opt = userService.findByUsername(username);
        if (opt.isEmpty()) throw new CustomErrorException(HttpStatus.NOT_FOUND, "User not found");
        User user = opt.get();
        //delete old image
        ProfilePicture profilePicture = user.getUserProfile().getProfilePicture();
        log.info("inside profile picture save {}", profilePicture != null);
        if (profilePicture != null) {
            log.info("profile picture is going to be deleted. {}", profilePicture.getPath() + "/" + profilePicture.getName());
            fileStore.deleteImage(profilePicture.getPath() + "/" + profilePicture.getName());
            profilePictureDAO.delete(profilePicture.getId());
            log.info("deleted old image {}", profilePicture.getId());
        }
        log.info("passed delete branch");
        //check if the file is empty
        FileValidator.validateFile(file);
        //get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        //Save Image in S3 and then save image in the database
        String path = String.format("%s/%s", Bucket.MY_BUCKET.getName(), UUID.randomUUID());
        String fileName = String.format("%s", file.getOriginalFilename());
        try {
            fileStore.upload(path, fileName, Optional.of(metadata), file.getInputStream());
            String url = fileStore.getUrl(path, fileName);
            log.info("profile image url : {}", url);
            ProfilePicture img = new ProfilePicture();
            img.setName(fileName);
            img.setPath(path);
            img.setUrl(url);
            ProfilePicture saved = profilePictureDAO.save(img);
            user.getUserProfile().setProfilePicture(saved);
            User updatedUser = userService.save(user);
            return updatedUser.getUserProfile().getProfilePicture();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
    }

    @Override
    public void deleteImageById(Long id) {
        profilePictureDAO.delete(id);
    }
}
