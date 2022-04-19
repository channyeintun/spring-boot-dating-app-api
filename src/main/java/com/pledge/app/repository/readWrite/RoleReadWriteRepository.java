package com.pledge.app.repository.readWrite;

import com.pledge.app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleReadWriteRepository extends JpaRepository<Role,Long> {
}
