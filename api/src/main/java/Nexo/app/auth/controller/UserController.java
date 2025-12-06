package Nexo.app.auth.controller;

import Nexo.app.auth.dto.*;
import Nexo.app.auth.model.User;
import Nexo.app.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;



    @PostMapping("/register")
    public UserResponse register(@RequestBody UserCreateRequest req){
        return userService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserLoginRequest req){
        return userService.login(req);
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }


    /*@PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest req) {
        return userService.refreshToken(req.refreshToken(), req.refreshToken());
    }*/
}
