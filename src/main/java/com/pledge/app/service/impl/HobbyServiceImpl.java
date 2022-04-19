package com.pledge.app.service.impl;

import com.pledge.app.dao.HobbyDAO;
import com.pledge.app.entity.Hobby;
import com.pledge.app.service.HobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HobbyServiceImpl implements HobbyService {

    @Autowired
    private HobbyDAO hobbyDAO;

    @Override
    public List<Hobby> findAll() {
        return hobbyDAO.findAll();
    }

    @Override
    public Hobby createHobby(Hobby hobby) {
        return hobbyDAO.save(hobby);
    }

    @Override
    public Hobby updateHobby(Hobby hobby) {
        return hobbyDAO.save(hobby);
    }

    @Override
    public void deleteHobby(Long id) {
        hobbyDAO.deleteById(id);
    }
}
