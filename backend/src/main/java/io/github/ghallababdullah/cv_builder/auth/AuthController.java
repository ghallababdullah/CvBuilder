package io.github.ghallababdullah.cv_builder.auth;

import io.github.ghallababdullah.cv_builder.auth.dto.LoginRequest;
import io.github.ghallababdullah.cv_builder.auth.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService ;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid
            @RequestBody
            LoginRequest  request
    ){

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);

    }
}
