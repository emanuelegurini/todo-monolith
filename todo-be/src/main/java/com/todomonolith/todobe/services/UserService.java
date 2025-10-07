package com.todomonolith.todobe.services;

import com.todomonolith.todobe.entities.Setting;
import com.todomonolith.todobe.entities.User;
import com.todomonolith.todobe.enums.AccountTypeEnum;
import com.todomonolith.todobe.enums.ThemeEnum;
import com.todomonolith.todobe.repositories.UserRepository;
import com.todomonolith.todobe.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

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

    public Optional<User> partialUpdate(Long id, Map<String, Object> updates) {
        return userRepository.findById(id)
                .map(user -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "name" -> user.setName((String) value);
                            case "surname" -> user.setSurname((String) value);
                            case "email" -> user.setEmail((String) value);
                        }
                    });
                    return userRepository.save(user);
                });
    }

    /**
     * STEP 4: Load User by Username (Email)
     *
     * This method is required by UserDetailsService interface.
     * Spring Security calls this method during authentication to load user details.
     *
     * FLOW:
     * 1. User submits login request with email and password
     * 2. Spring Security calls this method with the email
     * 3. We fetch the user from database
     * 4. We wrap it in UserDetailsImpl (which Spring Security understands)
     * 5. Spring Security compares the password from login with the one from database
     *
     * @param email The email of the user trying to login (we use email as username)
     * @return UserDetails object containing user information for authentication
     * @throws UsernameNotFoundException if user with given email doesn't exist
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try to find user by email in database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Wrap our User entity in UserDetailsImpl so Spring Security can use it
        return new UserDetailsImpl(user);
    }
}
