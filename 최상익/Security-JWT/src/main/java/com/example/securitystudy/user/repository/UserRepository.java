package com.example.securitystudy.user.repository;

import com.example.securitystudy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}