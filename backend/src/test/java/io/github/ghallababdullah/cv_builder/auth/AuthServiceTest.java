package io.github.ghallababdullah.cv_builder.auth;

import io.github.ghallababdullah.cv_builder.auth.dto.InvalidCredentialsException;
import io.github.ghallababdullah.cv_builder.auth.dto.LoginRequest;
import io.github.ghallababdullah.cv_builder.auth.dto.LoginResponse;
import io.github.ghallababdullah.cv_builder.user.User;
import io.github.ghallababdullah.cv_builder.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private  PasswordEncoder passwordEncoder ;
    @Mock
    private  JwtService jwtService ;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        // AuthService имеет поле @Value("${app.jwt.expiration-ms}") long expirationMs
        // В юнит-тесте @Value не сработает — устанавливаем через reflection
        ReflectionTestUtils.setField(authService, "expirationMs", 3600000L);
    }

    @Test
    void login_success_shouldReturnToken(){

        //arrange
        LoginRequest request = new LoginRequest ("test@example.com", "password123");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPasswordHash("hashed_password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);
        when(jwtService.generateToken(1L, "test@example.com")).thenReturn("fake.jwt.token");

        // ACT
        LoginResponse response = authService.login(request);

        // ASSERT
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("fake.jwt.token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiresIn()).isEqualTo(3600L);  // ms/1000
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getUserId()).isEqualTo(1L);
        // Проверяем что JwtService был вызван с правильными параметрами
        verify(jwtService).generateToken(1L, "test@example.com");
    }

    @Test
    void login_userNotFound_throwsInvalidCredentials() {
        // ARRANGE
        LoginRequest request = new LoginRequest("nonexistent@example.com", "any");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid email or password");

        // Проверяем что JwtService НЕ был вызван (защита от лишней работы)
        verify(jwtService, never()).generateToken(anyLong(), anyString());
    }

    @Test
    void login_wrongPassword_throwsInvalidCredentials() {
        // ARRANGE
        LoginRequest request = new LoginRequest("test@example.com", "wrongPassword");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPasswordHash("hashed_password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "hashed_password")).thenReturn(false);

        // ACT & ASSERT
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid email or password");

        // Проверяем что токен НЕ создавался
        verify(jwtService, never()).generateToken(anyLong(), anyString());
    }



}