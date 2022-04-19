package com.pledge.app.repository.readWrite;

import com.pledge.app.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesReadWriteRepository extends JpaRepository<Like,Long> {

}
