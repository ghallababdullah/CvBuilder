package io.github.ghallababdullah.cv_builder.auth;


import io.github.ghallababdullah.cv_builder.user.User;
import io.github.ghallababdullah.cv_builder.user.UserNotFoundException;
import io.github.ghallababdullah.cv_builder.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;
    /**
     * Возвращает email текущего залогиненного пользователя.
     * Если не залогинен — бросает исключение.
     */
    public String getCurrentUserEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user in security context");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();  // username = email
        }

        return principal.toString();
    }

    /**
     * Возвращает полный User объект текущего пользователя из БД.
     */
    public User getCurrentUser() {
        String email = getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(-1L));
    }

    /**
     * Возвращает ID текущего пользователя.
     */
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }



}
