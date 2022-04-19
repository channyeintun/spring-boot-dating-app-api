package com.pledge.app.dao;

import com.pledge.app.entity.Role;
import com.pledge.app.entity.RoleName;
import com.pledge.app.repository.readOnly.RoleReadOnlyRepository;
import com.pledge.app.repository.readWrite.RoleReadWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAO {
    private RoleReadOnlyRepository roleReadOnlyRepository;
    private RoleReadWriteRepository roleReadWriteRepository;

    @Autowired
    public RoleDAO(RoleReadOnlyRepository roleReadOnlyRepository,
                   RoleReadWriteRepository roleReadWriteRepository){
        this.roleReadOnlyRepository=roleReadOnlyRepository;
        this.roleReadWriteRepository=roleReadWriteRepository;
    }

    public Role findByName(RoleName name){
        return roleReadOnlyRepository.findByName(name);
    }

    public Boolean existsByName(RoleName name){
        return roleReadOnlyRepository.existsByName(name);
    }

    public void save(Role role){
        roleReadWriteRepository.saveAndFlush(role);
    }
}
