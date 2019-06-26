package ru.bmstu.coursework.domain.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    STUDENT,
    TEACHER;

    @Override
    public String getAuthority() {
        return name();
    }

}