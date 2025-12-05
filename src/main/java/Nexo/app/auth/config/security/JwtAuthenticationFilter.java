package Nexo.app.auth.config.security;

import Nexo.app.auth.config.jwt.JwtService;
import Nexo.app.auth.model.User;
import Nexo.app.auth.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(req, res);
            return;
        }

        String token = authHeader.substring(7);

        // validacion del tipo de token
        if (!jwtService.isAccessToken(token) || !jwtService.isTokenValid(token)){
            filterChain.doFilter(req, res);
            return;
        }

        String userId = jwtService.extractUserId(token);
        Optional<User> userOpt = userRepository.findById(java.util.UUID.fromString(userId));

        if (userOpt.isEmpty()){
            filterChain.doFilter(req, res);
            return;
        }

        User user = userOpt.get();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        user, null, java.util.Collections.emptyList()
                );

        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(req)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(req, res);


    }
}
