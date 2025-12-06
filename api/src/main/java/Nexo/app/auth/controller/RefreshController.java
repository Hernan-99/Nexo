package Nexo.app.auth.controller;

import Nexo.app.auth.dto.AuthResponse;
import Nexo.app.auth.dto.RefreshRequest;
import Nexo.app.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RefreshController {

    private final AuthService authService;
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest req){
        AuthResponse response = authService.refresh(req.refreshToken());
        return ResponseEntity.ok(response);
    }
}
