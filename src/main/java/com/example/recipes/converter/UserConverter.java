package com.example.recipes.converter;

import com.example.recipes.dto.UserDto;
import com.example.recipes.entity.UserEntity;

import java.util.Optional;

public class UserConverter {

    public static Optional<UserEntity> toEntity(UserDto userDto) {
        if (userDto == null) {
            return Optional.empty();
        }


        UserEntity userEntity = UserEntity.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .role("ROLE_USER")
                .build();

        return Optional.of(userEntity);
    }

    public static Optional<UserDto> toDto(UserEntity userEntity) {
        if (userEntity == null) {
            return Optional.empty();
        }

        UserDto userDto = UserDto.builder()
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .build();

        return Optional.of(userDto);
    }

}

