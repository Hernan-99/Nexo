package Nexo.app.auth.service;

import Nexo.app.auth.config.JwtService;
import Nexo.app.auth.dto.UserCreateRequest;
import Nexo.app.auth.dto.UserLoginRequest;
import Nexo.app.auth.dto.UserResponse;
import Nexo.app.auth.model.Role;
import Nexo.app.auth.model.User;
import Nexo.app.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
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
                user.getCreatedAt()
        );
    }

    @Override
    public String login(UserLoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(()-> new RuntimeException("Credenciales invalidas"));

        if (!passwordEncoder.matches(req.password(), user.getPassword())){
            throw new RuntimeException("Credenciales invalidas");
        }

        return  jwtService.generateToken(user);
    }
}
