package Nexo.app.auth.service;

import Nexo.app.auth.config.jwt.JwtService;
import Nexo.app.auth.dto.AuthResponse;
import Nexo.app.auth.model.User;
import Nexo.app.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthResponse refresh(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)){
            throw new RuntimeException("Invalid refresh token");
        }

        if (!"refresh".equals(jwtService.extractType(refreshToken))) {
            throw new RuntimeException("Token is not a refresh token");
        }

        String userId = jwtService.extractUserId(refreshToken);

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccess = jwtService.generateAccessToken(user);
        String newRefresh = jwtService.generateRefreshToken(user);

        return new AuthResponse(newAccess, newRefresh);
    }
}
