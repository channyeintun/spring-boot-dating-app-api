package com.pledge.app.service;

import com.pledge.app.entity.Hobby;

import java.util.List;

public interface HobbyService {
    public List<Hobby> findAll();

    public Hobby createHobby(Hobby hobby);

    public Hobby updateHobby(Hobby hobby);

    public void deleteHobby(Long id);
}
