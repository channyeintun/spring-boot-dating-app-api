package com.pledge.app.repository.readWrite;

import com.pledge.app.entity.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HobbyReadWriteRepository extends JpaRepository<Hobby,Long> {
}
