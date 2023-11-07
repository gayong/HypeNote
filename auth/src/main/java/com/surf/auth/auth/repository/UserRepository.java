package com.surf.auth.auth.repository;

import com.surf.auth.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Optional<User> findByNickName(String nickName);
    Optional<User> findByUserId(int userId);
}