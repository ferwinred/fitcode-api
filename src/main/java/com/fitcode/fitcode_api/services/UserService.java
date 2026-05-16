package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.dto.UserResponseDto;
import com.fitcode.fitcode_api.dto.UserRoutineDto;
import com.fitcode.fitcode_api.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto getUserByEmail(String email);

    UserResponseDto updateUser(Long id, UserUpdateDto dto);

    void softDeleteUser(Long id);

    UserRoutineDto assignRoutine(Long userId, Long routineId, UserRoutineDto data);
}
