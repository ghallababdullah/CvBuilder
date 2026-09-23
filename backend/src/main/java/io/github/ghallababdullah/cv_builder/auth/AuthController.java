package io.github.ghallababdullah.cv_builder.auth;

import io.github.ghallababdullah.cv_builder.auth.dto.LoginRequest;
import io.github.ghallababdullah.cv_builder.auth.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and JWT token management")
public class AuthController {

    private final AuthService authService ;

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user and get JWT token",
            description = "Verifies email and password. Returns JWT token valid for 1 hour."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, JWT returned"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password"),
            @ApiResponse(responseCode = "400", description = "Invalid request format")
    })
    public ResponseEntity<LoginResponse> login(
            @Valid
            @RequestBody
            LoginRequest  request
    ){

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);

    }
}
