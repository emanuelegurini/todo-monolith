package com.todomonolith.todobe.repositories;

import com.todomonolith.todobe.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
