package com.todomonolith.todobe.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "settings")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "theme")
    private String theme;

    @Column(name = "account_type")
    private String accountType;

    @OneToOne
    @JoinColumn(name = "user_id",  nullable = false, unique = true)
    private User user;
}
