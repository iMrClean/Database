package ru.bmstu.coursework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bmstu.coursework.domain.entities.Task;
import ru.bmstu.coursework.repository.TaskRepository;
import ru.bmstu.coursework.service.TaskService;
import ru.bmstu.coursework.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findAllStudentTasks() {
        return taskRepository.findAllByUserId(userService.getCurrentUser().getId());
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public void save(Task task) {
        Task taskToSave = new Task();
        if (task.getId() != null) {
            taskToSave.setId(task.getId());
        }
        taskToSave.setCategory(task.getCategory());
        taskToSave.setUser(userService.getCurrentUser());
        taskToSave.setTitle(task.getTitle());
        taskToSave.setDescription(task.getDescription());
        taskToSave.setPrice(task.getPrice());
        taskToSave.setDateTime(LocalDateTime.now());
        taskRepository.save(taskToSave);
    }

    @Override
    public void delete(Task task) {
        taskRepository.delete(task);
    }

}
