package ru.bmstu.coursework.service;

import ru.bmstu.coursework.domain.entities.Message;

import java.util.List;

public interface MessageService {

    void send(Message message, Long id);

    List<Message> findAllByTaskId(Long id);
}
