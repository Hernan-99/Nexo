package Nexo.app.api.tasks.controller;

import Nexo.app.api.tasks.dto.TaskRequest;
import Nexo.app.api.tasks.dto.TaskResponse;
import Nexo.app.api.tasks.dto.TaskUpdateRequest;
import Nexo.app.api.tasks.model.Task;
import Nexo.app.api.tasks.service.TaskService;
import Nexo.app.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponse create(
            @AuthenticationPrincipal User user,
            @RequestBody TaskRequest req
    ){
        return taskService.create(user, req);
    }

    @GetMapping
    public List<TaskResponse> findAll(@AuthenticationPrincipal User user) {
        return taskService.findAll(user);
    }

    @GetMapping("/{id}")
    public TaskResponse findById(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ){
        return taskService.findById(user, id);
    }

    @PatchMapping("/{id}")
    public TaskResponse update(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @RequestBody TaskUpdateRequest req
    ){
        return taskService.update(user, id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        taskService.delete(user, id);
    }
}
