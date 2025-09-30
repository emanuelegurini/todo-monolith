package com.todomonolith.todobe.services;

import com.todomonolith.todobe.entities.Project;
import com.todomonolith.todobe.repositories.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    public void save(Project project) {
        projectRepository.save(project);
    }
}


