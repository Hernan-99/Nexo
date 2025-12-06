package Nexo.app.api.tasks.service;

import Nexo.app.api.tasks.dto.TaskRequest;
import Nexo.app.api.tasks.dto.TaskResponse;
import Nexo.app.api.tasks.dto.TaskUpdateRequest;
import Nexo.app.api.tasks.model.Task;
import Nexo.app.api.tasks.repository.TaskRepository;
import Nexo.app.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;

    // utils - separar
    private TaskResponse toResponse(Task task){
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt()
        );
    }

    private Task getUserTaskOrThrow(User user, UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso a esta tarea");
        }
        return task;
    }

    @Override
    public TaskResponse create(User user, TaskRequest req) {
        Task task = Task.builder()
                .title(req.title())
                .description(req.description())
                .completed(false)
                .user(user)
                .build();

        taskRepository.save(task);
        return toResponse(task);
    }



    @Override
    public List<TaskResponse> findAll(User user) {
        return taskRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TaskResponse findById(User user, UUID id) {
        return toResponse(getUserTaskOrThrow(user, id));
    }

    @Override
    public TaskResponse update(User user, UUID id, TaskUpdateRequest req) {
        Task task = getUserTaskOrThrow(user, id);

        if (req.title() != null) task.setTitle(req.title());
        if (req.description() != null) task.setDescription(req.description());
        if (req.completed() != null) task.setCompleted(req.completed());

        taskRepository.save(task);
        return toResponse(task);
    }

    @Override
    public void delete(User user, UUID id) {
        Task task = getUserTaskOrThrow(user, id);
        taskRepository.delete(task);
    }
}
