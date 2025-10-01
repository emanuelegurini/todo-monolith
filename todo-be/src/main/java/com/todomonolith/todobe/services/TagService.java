package com.todomonolith.todobe.services;

import com.todomonolith.todobe.entities.Tag;
import com.todomonolith.todobe.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TagService {

    private TagRepository tagRepository;

    public Tag findById(long id) {
        return tagRepository.findById(id).orElse(null);
    }
}
