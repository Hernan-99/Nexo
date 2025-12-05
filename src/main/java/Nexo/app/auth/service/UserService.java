package Nexo.app.auth.service;

import Nexo.app.auth.dto.UserCreateRequest;
import Nexo.app.auth.dto.UserLoginRequest;
import Nexo.app.auth.dto.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponse register(UserCreateRequest req);
    String login(UserLoginRequest req); // retorna un JWT
}
