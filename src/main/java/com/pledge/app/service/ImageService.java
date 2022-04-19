package com.pledge.app.service;

import com.pledge.app.entity.User;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService {
    public User save(String username, MultipartFile[] files);
}
