package io.github.ghallababdullah.cv_builder.auth.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType;      // "Bearer"
    private Long expiresIn;         // в секундах
    private String email;
    private Long userId;
}
