package com.todomonolith.todobe.repositories;

import com.todomonolith.todobe.entities.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Long> {
}
