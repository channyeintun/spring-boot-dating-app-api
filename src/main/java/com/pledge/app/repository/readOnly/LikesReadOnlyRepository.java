package com.pledge.app.repository.readOnly;

import com.pledge.app.annotation.ReadOnlyRepository;
import com.pledge.app.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ReadOnlyRepository
public interface LikesReadOnlyRepository extends JpaRepository<Like,Long> {
    public List<Like> findByUserId(Long userId);
    public Like findByUserIdAndLikedUserId(Long userId, Long likedUserId);
    public List<Like> findByLikedUserId(Long likedUserId);
}
