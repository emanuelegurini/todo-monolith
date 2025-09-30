package com.todomonolith.todobe.services;

import com.todomonolith.todobe.entities.User;
import com.todomonolith.todobe.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    public User save(String name, String surname, String email) {
        System.out.println("starting saving user");
        var user = User.builder().name(name).surname(surname).email(email).build();
        userRepository.save(user);
        System.out.println(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
