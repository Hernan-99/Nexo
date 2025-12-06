package Nexo.app.auth.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
