package com.pledge.app.repository.readOnly;

import com.pledge.app.annotation.ReadOnlyRepository;
import com.pledge.app.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ReadOnlyRepository
public interface ImageReadOnlyRepository extends JpaRepository<Image,Long> {
}
