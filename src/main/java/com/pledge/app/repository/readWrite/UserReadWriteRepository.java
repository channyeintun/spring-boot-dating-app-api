package com.pledge.app.repository.readWrite;

import com.pledge.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserReadWriteRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    Page<User> findAll(Pageable pageable);

    @Query(value = "SELECT point FROM user WHERE username=:username ORDER BY name", nativeQuery = true)
    Long getPointByUsername(@Param("username") String username);

    @Query(value = "SELECT id FROM user WHERE username=:username", nativeQuery = true)
    Long getIdByUsername(@Param("username") String username);

    @Query(value = "SELECT COUNT(id) FROM user", nativeQuery = true)
    int countAll();

    @Transactional
    @Modifying
    @Query(value = "UPDATE user u SET u.fcmToken=:fcmToken WHERE u.username=:username")
    void updateFcmTokenByUsername(@Param("fcmToken") String fcmToken, @Param("username") String username);
}
