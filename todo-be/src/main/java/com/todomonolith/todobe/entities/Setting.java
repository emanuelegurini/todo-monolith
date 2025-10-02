package com.todomonolith.todobe.entities;

import com.todomonolith.todobe.enums.AccountTypeEnum;
import com.todomonolith.todobe.enums.ThemeEnum;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "theme")
    private ThemeEnum theme;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountTypeEnum accountType;

    @OneToOne
    @JoinColumn(name = "user_id",  nullable = false, unique = true)
    private User user;
}
