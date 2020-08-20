package com.task.booking.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    //Модель сущности "Роль пользователя"

    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
