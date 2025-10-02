package com.todomonolith.todobe.dto;

import com.todomonolith.todobe.enums.AccountTypeEnum;
import com.todomonolith.todobe.enums.ThemeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingDTO {
    //private Long id;
    private ThemeEnum theme;
    private AccountTypeEnum accountType;
}
