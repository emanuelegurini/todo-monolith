package com.todomonolith.todobe.entities;

import com.todomonolith.todobe.enums.TaskStatusEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private TaskStatusEnum priority;

    @ManyToOne
    @JoinColumn( name = "project_id")
    private Project project;
}
