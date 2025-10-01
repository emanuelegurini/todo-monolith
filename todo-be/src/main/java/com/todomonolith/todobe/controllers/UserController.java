package com.todomonolith.todobe.controllers;


import com.todomonolith.todobe.dto.UserDTO;
import com.todomonolith.todobe.entities.User;
import com.todomonolith.todobe.mappers.UserMapper;
import com.todomonolith.todobe.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userMapper.toDTOList((List<User>) userService.findAll());
    }

}
