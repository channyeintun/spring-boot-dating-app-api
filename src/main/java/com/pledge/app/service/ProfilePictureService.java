package com.pledge.app.service;

import com.pledge.app.entity.ProfilePicture;
import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureService {
    public ProfilePicture save(MultipartFile file, String username);
    public void deleteImageById(Long id);
}
