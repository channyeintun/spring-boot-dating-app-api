package com.pledge.app.repository.readWrite;

import com.pledge.app.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageReadWriteRepository extends JpaRepository<Image,Long> {
}
