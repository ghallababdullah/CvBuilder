package io.github.ghallababdullah.cv_builder.auth.dto;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
