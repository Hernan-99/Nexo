package Nexo.app.auth.dto;

public record UserCreateRequest(
        String name,
        String email,
        String password
) {
}
