package com.todomonolith.todobe.repositories;

import com.todomonolith.todobe.entities.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
