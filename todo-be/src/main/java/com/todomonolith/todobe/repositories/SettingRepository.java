package com.todomonolith.todobe.repositories;

import com.todomonolith.todobe.entities.Setting;
import org.springframework.data.repository.CrudRepository;

public interface SettingRepository extends CrudRepository<Setting, Long> {
}
