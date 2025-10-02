package com.todomonolith.todobe.controllers;


import com.todomonolith.todobe.dto.UserDTO;
import com.todomonolith.todobe.entities.User;
import com.todomonolith.todobe.mappers.UserMapper;
import com.todomonolith.todobe.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userMapper.toDTOList((List<User>) userService.findAll());
    }

    @GetMapping("/users/{id}")
    public Optional<UserDTO> getUser(@PathVariable Long id) {
        return userService.findById(id).map(userMapper::toDTO);
    }

}
