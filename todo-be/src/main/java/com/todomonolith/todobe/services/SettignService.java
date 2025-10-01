package com.todomonolith.todobe.services;

import com.todomonolith.todobe.entities.Setting;
import com.todomonolith.todobe.repositories.SettingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SettignService {

    private SettingRepository settingRepository;

    public Setting findById(long id) {
        return settingRepository.findById(id).orElse(null);
    }
}
