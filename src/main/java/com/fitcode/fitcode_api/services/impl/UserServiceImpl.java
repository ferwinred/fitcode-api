package com.fitcode.fitcode_api.services.impl;

import com.fitcode.fitcode_api.dto.UserResponseDto;
import com.fitcode.fitcode_api.dto.UserRoutineDto;
import com.fitcode.fitcode_api.dto.UserUpdateDto;
import com.fitcode.fitcode_api.exceptions.ResourceNotFoundException;
import com.fitcode.fitcode_api.models.Role;
import com.fitcode.fitcode_api.models.Routine;
import com.fitcode.fitcode_api.models.User;
import com.fitcode.fitcode_api.models.UserRoutine;
import com.fitcode.fitcode_api.repository.UserRepository;
import com.fitcode.fitcode_api.repository.UserRoutineRepository;
import com.fitcode.fitcode_api.repository.RoleRepository; // opcional si actualizas role
import com.fitcode.fitcode_api.repository.RoutineRepository;
import com.fitcode.fitcode_api.services.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // inyectado desde SecurityConfig
    private final RoleRepository roleRepository; // si no actualizas role, elimina esta dependencia
    private final UserRoutineRepository userRoutineRepository; // para asignar rutina
    private final RoutineRepository routineRepository; // para asignar rutina

    private UserResponseDto toDto(User u) {
        return UserResponseDto.builder()
                .id(u.getId())
                .fullName(u.getFullName())
                .displayName(u.getDisplayName())
                .email(u.getEmail())
                .dateOfBirth(u.getDateOfBirth())
                .sex(u.getSex())
                .heightCm(u.getHeightCm())
                .weightKg(u.getWeightKg())
                .metadata(u.getMetadata())
                .role(u.getRole() != null ? u.getRole().getId().longValue() : null)
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAllActive()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserRoutineDto assignRoutine(Long userId, Long routineId, UserRoutineDto data) {
        Optional<Routine> routineOpt = routineRepository.findById(routineId);
        Optional<User> userOpt = userRepository.findActiveById(userId);

        if (routineOpt.isEmpty()) {
            throw new ResourceNotFoundException("Rutina no encontrada con id: " + routineId);
        }
        if (userRoutineRepository.findByUserIdAndRoutineId(userId, routineId).isPresent()) {
            throw new RuntimeException("El usuario ya tiene asignada esta rutina");
        }

        Routine routine = routineOpt.get();
        User user = userOpt.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));

        UserRoutine userRoutine = new UserRoutine();
        userRoutine.setUser(user);
        userRoutine.setRoutine(routine);
        userRoutine.setStartDate(LocalDateTime.now().toLocalDate());
        userRoutine.setProgressPercent(0);
        userRoutine.setStatus("active");

        userRoutine = userRoutineRepository.save(userRoutine);
        // Lógica para asignar rutina al usuario
        UserRoutineDto userRoutineDto = UserRoutineDto.builder()
                .id(userRoutine.getId())
                .userId(userRoutine.getUser().getId())
                .routineId(userRoutine.getRoutine().getId())
                .startDate(userRoutine.getStartDate())
                .endDate(userRoutine.getEndDate())
                .progressPercent(userRoutine.getProgressPercent())
                .status(userRoutine.getStatus())
                .build();

        return userRoutineDto;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User u = userRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return toDto(u);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        User u = userRepository.findActiveByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return toDto(u);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateDto dto) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        if (dto.getFullName() != null)
            user.setFullName(dto.getFullName());
        if (dto.getDisplayName() != null)
            user.setDisplayName(dto.getDisplayName());
        if (dto.getEmail() != null)
            user.setEmail(dto.getEmail());
        if (dto.getDateOfBirth() != null)
            user.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getSex() != null)
            user.setSex(dto.getSex());
        if (dto.getHeightCm() != null)
            user.setHeightCm(dto.getHeightCm());
        if (dto.getWeightKg() != null)
            user.setWeightKg(dto.getWeightKg());
        if (dto.getMetadata() != null)
            user.setMetadata(dto.getMetadata());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String hashed = passwordEncoder.encode(dto.getPassword());
            user.setPasswordHash(hashed);
        }

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + dto.getRoleId()));
            user.setRole(role);
        }

        User saved = userRepository.save(user);
        return toDto(saved);
    }

    @Override
    public void softDeleteUser(Long id) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
