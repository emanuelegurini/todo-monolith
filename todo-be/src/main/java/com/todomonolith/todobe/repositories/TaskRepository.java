package com.todomonolith.todobe.repositories;

import com.todomonolith.todobe.entities.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
