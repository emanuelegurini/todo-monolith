package com.todomonolith.todobe.services;

import com.todomonolith.todobe.entities.Task;
import com.todomonolith.todobe.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TaskService {

    private TaskRepository taskRepository;

    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
