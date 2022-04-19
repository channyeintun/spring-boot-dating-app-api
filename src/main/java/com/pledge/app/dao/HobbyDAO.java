package com.pledge.app.dao;

import com.pledge.app.entity.Hobby;
import com.pledge.app.repository.readOnly.HobbyReadOnlyRepository;
import com.pledge.app.repository.readWrite.HobbyReadWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HobbyDAO {
    private HobbyReadOnlyRepository hobbyReadOnlyRepository;
    private HobbyReadWriteRepository hobbyReadWriteRepository;

    @Autowired
    public HobbyDAO(HobbyReadOnlyRepository hobbyReadOnlyRepository,
                    HobbyReadWriteRepository hobbyReadWriteRepository) {
        this.hobbyReadOnlyRepository = hobbyReadOnlyRepository;
        this.hobbyReadWriteRepository = hobbyReadWriteRepository;
    }

    public List<Hobby> findAll(){
        return hobbyReadOnlyRepository.findAll();
    }

    public Hobby save(Hobby hobby) {
        return hobbyReadWriteRepository.saveAndFlush(hobby);
    }

    public void deleteById(Long id){
        hobbyReadWriteRepository.deleteById(id);
    }
}
