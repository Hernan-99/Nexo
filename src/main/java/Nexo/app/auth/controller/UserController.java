package Nexo.app.auth.controller;

import Nexo.app.auth.dto.UserCreateRequest;
import Nexo.app.auth.dto.UserLoginRequest;
import Nexo.app.auth.dto.UserResponse;
import Nexo.app.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String login(@RequestBody UserLoginRequest req){
        return userService.login(req);
    }
}
