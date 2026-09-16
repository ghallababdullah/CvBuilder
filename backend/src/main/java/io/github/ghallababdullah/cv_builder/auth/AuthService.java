package io.github.ghallababdullah.cv_builder.auth;

import io.github.ghallababdullah.cv_builder.auth.dto.InvalidCredentialsException;
import io.github.ghallababdullah.cv_builder.auth.dto.LoginRequest;
import io.github.ghallababdullah.cv_builder.auth.dto.LoginResponse;
import io.github.ghallababdullah.cv_builder.user.User;
import io.github.ghallababdullah.cv_builder.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder ;
    private final JwtService jwtService ;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs ;

    public LoginResponse login(LoginRequest request){
        log.info("Login attempt for email: {}", request.getEmail());

        // first we find the user , that was included in the request
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new InvalidCredentialsException());

        // if the user was found , we check the password
        // the passwordencoder needs two arguments which are the password from the request and the has password in the db
        if (!passwordEncoder.matches(request.getPassword(),user.getPasswordHash())){
            log.warn("Invalid password for email: {}", request.getEmail());
            throw new InvalidCredentialsException();
        }

        // if all good, we create the jwt token
        String token = jwtService.generateToken(user.getId(), user.getEmail());

        log.info("User logged in successfully: {}", user.getEmail());

        return new LoginResponse(
                token,
                "Bearer",
                expirationMs / 1000,  // конвертируем мс в секунды
                user.getEmail(),
                user.getId()
        );


    }


}
