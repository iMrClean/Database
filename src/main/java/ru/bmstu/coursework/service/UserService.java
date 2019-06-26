package ru.bmstu.coursework.service;

import ru.bmstu.coursework.domain.entities.User;

public interface UserService {

    User findUserByEmail(String email);

    void save(User user);

    User getCurrentUser();

}
