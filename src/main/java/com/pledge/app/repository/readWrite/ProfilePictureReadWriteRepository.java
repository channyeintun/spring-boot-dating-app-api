package com.pledge.app.repository.readWrite;

import com.pledge.app.entity.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePictureReadWriteRepository extends JpaRepository<ProfilePicture,Long> {
}
