package ru.bmstu.coursework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bmstu.coursework.domain.entities.Message;
import ru.bmstu.coursework.repository.MessageRepository;
import ru.bmstu.coursework.service.MessageService;
import ru.bmstu.coursework.service.TaskService;
import ru.bmstu.coursework.service.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    private final UserService userService;

    private final TaskService taskService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserService userService, TaskService taskService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.taskService = taskService;
    }

    @Override
    public void send(Message message, Long id) {
        message.setDateTime(LocalDateTime.now());
        message.setSender(userService.getCurrentUser());
        message.setTask(taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id)));
        messageRepository.save(message);
    }

    public List<Message> findAllByTaskId(Long id) {
        return messageRepository.findAllByTaskId(id);
    }

}
