package com.todomonolith.todobe.mappers;

import com.todomonolith.todobe.dto.SettingDTO;
import com.todomonolith.todobe.dto.UserDTO;
import com.todomonolith.todobe.entities.Setting;
import com.todomonolith.todobe.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

       return UserDTO.builder()
               .id(user.getId())
               .name(user.getName())
               .surname(user.getSurname())
               .email(user.getEmail())
               .setting(toSettingDTO(user.getSetting()))
               .build();
    }

    public List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private SettingDTO toSettingDTO(Setting setting) {
        if (setting == null) {
            return null;
        }

        return SettingDTO.builder()
                //.id(setting.getId())
                .theme(setting.getTheme())
                .accountType(setting.getAccountType())
                .build();
    }



}
