package Nexo.app.api.tasks.controller;

import Nexo.app.api.tasks.dto.TaskRequest;
import Nexo.app.api.tasks.dto.TaskResponse;
import Nexo.app.api.tasks.service.AudioService;
import Nexo.app.api.tasks.service.TaskService;
import Nexo.app.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/audio")
@RequiredArgsConstructor
public class AudioController {
    private final TaskService taskService;
    private final AudioService audioService;

    @PostMapping("/dictate")
    public TaskResponse dictateTask(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String text = audioService.transcribeAudio(file);
        TaskRequest req = new TaskRequest(text, ""); // ver como agregar la descripcion por audio

        return taskService.create(user, req);
    }
}
