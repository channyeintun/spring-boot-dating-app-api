package com.pledge.app.repository.readOnly;

import com.pledge.app.annotation.ReadOnlyRepository;
import com.pledge.app.entity.Role;
import com.pledge.app.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ReadOnlyRepository
public interface RoleReadOnlyRepository extends JpaRepository<Role,Long> {
    Role findByName(RoleName name);
    Boolean existsByName(RoleName name);
}
