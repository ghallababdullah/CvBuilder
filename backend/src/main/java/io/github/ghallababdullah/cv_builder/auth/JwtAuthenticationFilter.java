package io.github.ghallababdullah.cv_builder.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Извлечь токен из header
        String token = extractToken(request);

        // если токена нет - ничего страшного пройдем дальше, так как может быть публичный endpoint
        try {
            if(!jwtService.isValid(token)){
                log.warn("Invalid JWT token in request to {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }
// 4. Извлечь email из токена
            String email = jwtService.extractEmail(token);


            // 5. Проверить что ещё не аутентифицирован (защита от повторной)
            if (SecurityContextHolder.getContext().getAuthentication()==null){


                // 6. Загрузить UserDetails из БД
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);


                //               7. Создать Authentication объект
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,  // credentials = null (пароль уже проверен)
                                userDetails.getAuthorities()
                        );
                // 8. Добавить детали запроса (IP, session id)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 9. Положить в SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("Authenticated user: {}", email);


            }



        }catch (Exception e) {
            log.error("Cannot authenticate user", e);
        }
        // 10. Пропустить запрос дальше
        filterChain.doFilter(request, response);


    }
    /**
     * Извлекает токен из header "Authorization: Bearer <token>"
     */
    private String extractToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // убираем "Bearer "
        }
        return null ;

    }
}
