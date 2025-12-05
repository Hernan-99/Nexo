package Nexo.app.auth.service;

import Nexo.app.auth.config.jwt.JwtService;
import Nexo.app.auth.dto.AuthResponse;
import Nexo.app.auth.dto.UserCreateRequest;
import Nexo.app.auth.dto.UserLoginRequest;
import Nexo.app.auth.dto.UserResponse;
import Nexo.app.auth.model.Role;
import Nexo.app.auth.model.User;
import Nexo.app.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserResponse register(UserCreateRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new RuntimeException("El email ya se encuentra registrado en el sistema");
        }

        User user = User.builder()
                .name(req.name())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }

    @Override
    public AuthResponse login(UserLoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(()-> new RuntimeException("Credenciales invalidas"));

        if (!passwordEncoder.matches(req.password(), user.getPassword())){
            throw new RuntimeException("Credenciales invalidas");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // nuestro username es el email
        return userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("No logramos encontrar ningun usuarion con el email: " + email));
    }

    @Override
    public AuthResponse refreshToken(String accessToken, String refreshToken){
        if (!jwtService.isTokenValid(refreshToken)){
            throw new RuntimeException("Refresh token invlido");
        }

        if (!jwtService.isRefreshToken(refreshToken)){
            throw new RuntimeException("No es un refresh token");
        }

        String userId = jwtService.extractUserId(refreshToken);
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(()->new RuntimeException("Usuario no encontrado"));

        String newAccess = jwtService.generateAccessToken(user);
        String newRefresh = jwtService.generateRefreshToken(user);

        return new AuthResponse(newAccess, newRefresh);
    }
}
