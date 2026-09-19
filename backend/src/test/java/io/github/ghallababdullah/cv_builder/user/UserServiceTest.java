package io.github.ghallababdullah.cv_builder.user;

import io.github.ghallababdullah.cv_builder.user.dto.RegisterUserRequest;
import io.github.ghallababdullah.cv_builder.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository ;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private  UserService userService;

    @Test
    void register_success_shouldReturnUserResponse() {
        //Arrange
        RegisterUserRequest request = new RegisterUserRequest(
                "test@example.com",
                "password123",
                "Test User"
        );

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password_123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("test@example.com");
        savedUser.setPasswordHash("hashed_password_123");
        savedUser.setFullName("Test User");
        savedUser.setCreatedAt(Instant.now());
        savedUser.setUpdatedAt(Instant.now());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // ACT
        UserResponse response = userService.register(request);

        // ASSERT
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getFullName()).isEqualTo("Test User");
    }

    @Test
    void  register_emailAlreadyExists_throwsException(){
        //Arrange
        RegisterUserRequest request = new RegisterUserRequest(
                "test@example.com",
                "password123",
                "Test User"
        );


        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // ACT & ASSERT
        assertThatThrownBy(()->userService.register(request))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("User with email test@example.com already exists");;

    }
    @Test
    void register_shouldHashPassword(){
        // this test is for to check the behavior
        //Arrange
        RegisterUserRequest request= new RegisterUserRequest(
            "test@example.com", "myPlainPassword",
                    "Test User"
        );

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_result");
        when(userRepository.save(any(User.class))).thenAnswer(invocation ->{
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        //Act

        userService.register(request);
        // ASSERT — проверяем что encode был вызван с правильным паролем
        verify(passwordEncoder).encode("myPlainPassword");

        // ASSERT — проверяем что encode был вызван РОВНО 1 раз
        verify(passwordEncoder, times(1)).encode(anyString());
    }
}