package ru.bmstu.coursework.domain.enums;

public enum Category {

    COURSEWORK,
    GRADUATION_THESIS,
    HOMEWORK;

    public String value() {
        return name();
    }

}
