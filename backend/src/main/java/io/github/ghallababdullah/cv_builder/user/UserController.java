package io.github.ghallababdullah.cv_builder.user;

import io.github.ghallababdullah.cv_builder.user.dto.RegisterUserRequest;
import io.github.ghallababdullah.cv_builder.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User registration and profile management")
public class UserController {

    private final UserService userService ;
    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account. Email must be unique. Password is hashed with bcrypt."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data (email format, password too short)"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<UserResponse>createUser(@Valid @RequestBody RegisterUserRequest req){
      UserResponse response=  userService.register(req);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Returns user information. Password hash is not exposed."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> getUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id){
        UserResponse res = userService.getById(id);
        return ResponseEntity.ok(res);
    }

}

