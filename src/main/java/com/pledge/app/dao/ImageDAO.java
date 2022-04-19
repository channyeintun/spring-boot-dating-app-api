package com.pledge.app.dao;

import com.pledge.app.entity.Image;
import com.pledge.app.repository.readOnly.ImageReadOnlyRepository;
import com.pledge.app.repository.readWrite.ImageReadWriteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ImageDAO {
    private ImageReadOnlyRepository imageReadOnlyRepository;
    private ImageReadWriteRepository imageReadWriteRepository;

    public ImageDAO(ImageReadOnlyRepository imageReadOnlyRepository,
                    ImageReadWriteRepository imageReadWriteRepository) {
        this.imageReadOnlyRepository = imageReadOnlyRepository;
        this.imageReadWriteRepository = imageReadWriteRepository;
    }

    public Image save(Image image) {
        return this.imageReadWriteRepository.save(image);
    }
}
