package com.pledge.app.repository.readOnly;

import com.pledge.app.annotation.ReadOnlyRepository;
import com.pledge.app.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ReadOnlyRepository
public interface MatchReadOnlyRepository extends JpaRepository<Match,Long>{
    public List<Match> findByUserId(Long userId);
    public List<Match> findByUserIdOrMatchedUserId(Long userId, Long matchedUserId);
}
