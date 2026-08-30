package io.github.ghallababdullah.cv_builder.user;

import io.github.ghallababdullah.cv_builder.user.dto.RegisterUserRequest;
import io.github.ghallababdullah.cv_builder.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterUserRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        // 1. Проверить, что email не занят
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: email {} already exists", request.getEmail());
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        // 2. Захешировать пароль
        String passwordHash = passwordEncoder.encode(request.getPassword());

        // 3. Создать User entity
        User user = new User();
        Instant now = Instant.now();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash(passwordHash);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        // 4. Сохранить в БД
        User saved = userRepository.save(user);
        log.info("User registered successfully with id: {}", saved.getId());

        // 5. Вернуть DTO (без пароля!)
        return new UserResponse(saved.getEmail(), saved.getFullName(), saved.getId(), saved.getCreatedAt());
    }
    public UserResponse getById(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        return new UserResponse(user.getEmail(), user.getFullName(), user.getId(), user.getCreatedAt());
    }
}
