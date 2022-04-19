package com.pledge.app.dao;

import com.pledge.app.entity.Image;
import com.pledge.app.entity.ProfilePicture;
import com.pledge.app.repository.readOnly.ProfilePictureReadOnlyRepository;
import com.pledge.app.repository.readWrite.ProfilePictureReadWriteRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProfilePictureDAO {
    private ProfilePictureReadOnlyRepository profilePictureReadOnlyRepository;
    private ProfilePictureReadWriteRepository profilePictureReadWriteRepository;

    public ProfilePictureDAO(ProfilePictureReadOnlyRepository profilePictureReadOnlyRepository,
                             ProfilePictureReadWriteRepository profilePictureReadWriteRepository){
        this.profilePictureReadOnlyRepository=profilePictureReadOnlyRepository;
        this.profilePictureReadWriteRepository=profilePictureReadWriteRepository;
    }

    public ProfilePicture save(ProfilePicture image) {
        return this.profilePictureReadWriteRepository.save(image);
    }

    public void delete(Long id) {
        this.profilePictureReadWriteRepository.deleteById(id);
    }
}
