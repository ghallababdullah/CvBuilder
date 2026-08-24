package io.github.ghallababdullah.cv_builder.user;

import io.github.ghallababdullah.cv_builder.user.dto.RegisterUserRequest;
import io.github.ghallababdullah.cv_builder.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService ;
    @PostMapping("/register")
    public ResponseEntity<UserResponse>createUser(@Valid @RequestBody RegisterUserRequest req){
      UserResponse response=  userService.register(req);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

