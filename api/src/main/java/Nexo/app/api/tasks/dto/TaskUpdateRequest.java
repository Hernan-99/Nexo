package Nexo.app.api.tasks.dto;

public record TaskUpdateRequest(
        String title,
        String description,
        Boolean completed
) {
}
