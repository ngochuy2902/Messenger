package com.msg.messenger.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RoleType {

    ADMIN(1L), USER(2L);

    private Long roleId;
}
