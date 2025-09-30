package com.todomonolith.todobe;

import com.todomonolith.todobe.entities.Project;
import com.todomonolith.todobe.entities.User;
import com.todomonolith.todobe.services.ProjectService;
import com.todomonolith.todobe.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("===============================");
        System.out.println("Spring Boot Application Started");
        System.out.println("===============================");
        ApplicationContext context = SpringApplication.run(Main.class, args);
        var userService = context.getBean(UserService.class);
        var projectService = context.getBean(ProjectService.class);
        userService.save("Maurizio", "Mario", "email@email");

        var project = Project.builder().name("pasta").description("bla bla bla").user(userService.save("Maurizio", "Mario", "email@email")).build();
        projectService.save(project);

    }
}