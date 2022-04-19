package com.pledge.app.repository.readOnly;

import com.pledge.app.annotation.ReadOnlyRepository;
import com.pledge.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@ReadOnlyRepository
@Repository
public interface UserReadOnlyRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT point FROM user WHERE username=:username ORDER BY name", nativeQuery = true)
    Long getPointByUsername(@Param("username") String username);

    @Query(value="SELECT id FROM user WHERE username=:username",nativeQuery = true)
    Long getIdByUsername(@Param("username") String username);

    @Query(value="SELECT COUNT(id) FROM user",nativeQuery = true)
    int countAll();

    String getFcmTokenByUserId(Long userId);
}
