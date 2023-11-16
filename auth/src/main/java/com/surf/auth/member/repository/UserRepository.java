package com.surf.auth.member.repository;

import com.surf.auth.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Optional<User> findByNickName(String nickName);
    Optional<User> findByUserPk(int userPk);
}