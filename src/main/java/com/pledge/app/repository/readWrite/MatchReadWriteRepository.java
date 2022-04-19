package com.pledge.app.repository.readWrite;

import com.pledge.app.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchReadWriteRepository extends JpaRepository<Match,Long> {

}
