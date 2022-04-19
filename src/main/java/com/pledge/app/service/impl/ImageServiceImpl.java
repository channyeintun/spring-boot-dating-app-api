package com.pledge.app.service.impl;

import com.pledge.app.config.s3.BucketName;
import com.pledge.app.dao.ImageDAO;
import com.pledge.app.entity.Image;
import com.pledge.app.entity.User;
import com.pledge.app.service.FileStore;
import com.pledge.app.service.ImageService;
import com.pledge.app.service.UserService;
import com.pledge.app.util.FileValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDAO imageDAO;

    @Autowired
    private UserService userService;
    @Autowired
    FileStore fileStore;

    @Override
    public User save(String username, MultipartFile[] files) {
        Optional<User> opt = userService.findByUsername(username);
        if (opt.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = opt.get();
        Set<Image> images = new HashSet<>();
        //delete old images
        user.getUserProfile().getImages().size();
        Set<Image> oldImages = user.getUserProfile().getImages();
        for (Image image : oldImages) {
            fileStore.deleteImage(image.getPath() + "/" + image.getName());
            log.info("deleted image {}", image.getId());
        }
        user.getUserProfile().getImages().removeAll(oldImages);
        log.info("deleted images from db");
        //save new images
        for (MultipartFile file : files) {
            //check if the file is empty
            FileValidator.validateFile(file);
            //get file metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));
            //Save Image in S3 and then save image in the database
            String path = String.format("%s/%s", BucketName.USER_IMAGE.getBucketName(), UUID.randomUUID());
            String fileName = String.format("%s", file.getOriginalFilename());
            try {
                fileStore.upload(path, fileName, Optional.of(metadata), file.getInputStream());
                String url = fileStore.getUrl(path, fileName);
                log.info("image url : {}", url);
                Image img = new Image();
                img.setName(fileName);
                img.setPath(path);
                img.setUrl(url);
                Image savedImage = imageDAO.save(img);
                images.add(savedImage);

            } catch (IOException e) {
                throw new IllegalStateException("Failed to upload file", e);
            }
        }
        user.getUserProfile().setImages(images);
        User updatedUser = userService.save(user);
        return updatedUser;
    }
}
