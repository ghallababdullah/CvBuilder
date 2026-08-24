package io.github.ghallababdullah.cv_builder.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String email ;

    private String fullName ;

    private Long id;

    private Instant createdAt ;


}
