package com.todomonolith.todobe.controllers;


import com.todomonolith.todobe.dto.UserDTO;
import com.todomonolith.todobe.entities.User;
import com.todomonolith.todobe.mappers.UserMapper;
import com.todomonolith.todobe.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userMapper.toDTOList((List<User>) userService.findAll());
    }

    @GetMapping("/{id}")
    public Optional<UserDTO> getUser(@PathVariable Long id) {
        return userService.findById(id).map(userMapper::toDTO);
    }

    @GetMapping("/user/{email}")
    public Optional<UserDTO> getUser(@PathVariable String email) {
        return userService.findByEmail(email).map(userMapper::toDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
       userService.deleteById(id);
       return ResponseEntity.noContent().build();
    }

}
