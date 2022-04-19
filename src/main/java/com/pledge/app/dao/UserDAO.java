package com.pledge.app.dao;

import com.pledge.app.entity.User;
import com.pledge.app.repository.readOnly.UserReadOnlyRepository;
import com.pledge.app.repository.readWrite.UserReadWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAO {
    @Autowired
    private UserReadOnlyRepository userReadOnlyRepository;
    @Autowired
    private UserReadWriteRepository userReadWriteRepository;

    public Long getIdByUsername(String username){
        return userReadOnlyRepository.getIdByUsername(username);
    }

    public List<User> findAll(int pageNo, int size){
        Pageable pageable=PageRequest.of(pageNo, size,
                Sort.by(Sort.DEFAULT_DIRECTION.ASC,"name"));
        Page<User> pagedResult=this.userReadOnlyRepository
                .findAll(pageable);
        return pagedResult.hasContent()?pagedResult.getContent():new ArrayList<>();
    }

    public Optional<User> findByUsername(String username){
        return this.userReadOnlyRepository.findByUsername(username);
    }

    public List<User> findAll(){
        return this.userReadOnlyRepository.findAll();
    }

    public Optional<User> findById(Long id){
        return userReadOnlyRepository.findById(id);
    }

    public User save(User user){
        return userReadWriteRepository.saveAndFlush(user);
    }

    public Long getPointByUsername(String username){
        return userReadOnlyRepository.getPointByUsername(username);
    }

    public int countAll() {
        return this.userReadOnlyRepository.countAll();
    }

    public void updateFcmToken(String token,String username){
        this.userReadWriteRepository.updateFcmTokenByUsername(token,username);
    }

    public String getFcmTokenByUserId(Long userId){
        return this.userReadOnlyRepository.getFcmTokenByUserId(userId);
    }
}
