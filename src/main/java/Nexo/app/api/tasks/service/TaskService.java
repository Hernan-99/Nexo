package Nexo.app.api.tasks.service;

import Nexo.app.api.tasks.dto.TaskRequest;
import Nexo.app.api.tasks.dto.TaskResponse;
import Nexo.app.api.tasks.dto.TaskUpdateRequest;
import Nexo.app.auth.model.User;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskResponse create(User user, TaskRequest req);
    List<TaskResponse> findAll(User user);
    TaskResponse findById(User user, UUID id);
    TaskResponse update(User user, UUID id, TaskUpdateRequest req);
    void delete(User user, UUID id);
}
