package com.example.recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.recipes.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

}
