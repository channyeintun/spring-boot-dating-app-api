package com.pledge.app.dao;

import com.pledge.app.entity.Like;
import com.pledge.app.repository.readOnly.LikesReadOnlyRepository;
import com.pledge.app.repository.readWrite.LikesReadWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikeDAO {
    private LikesReadOnlyRepository likesReadOnlyRepository;
    private LikesReadWriteRepository likesReadWriteRepository;

    @Autowired
    public LikeDAO(LikesReadOnlyRepository likesReadOnlyRepository,
                   LikesReadWriteRepository likesReadWriteRepository){
        this.likesReadOnlyRepository=likesReadOnlyRepository;
        this.likesReadWriteRepository=likesReadWriteRepository;
    }
    public List<Like> findByUserId(Long userId){
        return likesReadOnlyRepository.findByUserId(userId);
    }

    public Like findByUserIdAndLikedUserId(Long userId, Long likedUserId){
        return likesReadOnlyRepository.findByUserIdAndLikedUserId(userId,likedUserId);
    }

    public Like save(Like like){
        return likesReadWriteRepository.save(like);
    }

    public List<Like> findByLikedUserId(Long likedUserId){
        return likesReadOnlyRepository.findByLikedUserId(likedUserId);
    }

    public void deleteAll(){
        likesReadWriteRepository.deleteAll();
    }
}
