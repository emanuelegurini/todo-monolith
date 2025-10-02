package com.todomonolith.todobe.services;

import com.todomonolith.todobe.entities.Setting;
import com.todomonolith.todobe.entities.User;
import com.todomonolith.todobe.enums.AccountTypeEnum;
import com.todomonolith.todobe.enums.ThemeEnum;
import com.todomonolith.todobe.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    public void save(User user) {

        var defaultSetting = Setting.builder()
                .theme(ThemeEnum.LIGHT)
                .accountType(AccountTypeEnum.FREE)
                .user(user)
                .build();

        user.setSetting(defaultSetting);

        userRepository.save(user);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
