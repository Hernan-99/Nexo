package Nexo.app.auth.dto;

public record UserLoginRequest(
        String email,
        String password
) {
}
