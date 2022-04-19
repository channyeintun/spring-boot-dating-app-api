package com.pledge.app.dao;

import com.pledge.app.entity.Match;
import com.pledge.app.repository.readOnly.MatchReadOnlyRepository;
import com.pledge.app.repository.readWrite.MatchReadWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchDAO {
    private MatchReadOnlyRepository matchReadOnlyRepository;
    private MatchReadWriteRepository matchReadWriteRepository;

    @Autowired
    public MatchDAO(MatchReadOnlyRepository matchReadOnlyRepository,
                    MatchReadWriteRepository matchReadWriteRepository){
        this.matchReadOnlyRepository=matchReadOnlyRepository;
        this.matchReadWriteRepository=matchReadWriteRepository;
    }

    public void deleteAll(){
        matchReadWriteRepository.deleteAll();
    }

    public List<Match> findByUserIdOrMatchedUserId(Long userId, Long matchedUserId){
        return matchReadOnlyRepository.findByUserIdOrMatchedUserId(userId,matchedUserId);
    }

    public Match save(Match match){
        return matchReadWriteRepository.saveAndFlush(match);
    }
}
