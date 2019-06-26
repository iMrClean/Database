package ru.bmstu.coursework.service;

import ru.bmstu.coursework.domain.entities.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> findAll();

    List<Task> findAllStudentTasks();

    Optional<Task> findById(Long id);

    void save(Task task);

    void delete(Task task);

}
