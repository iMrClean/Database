package ru.bmstu.coursework.service;

import ru.bmstu.coursework.domain.entities.Order;

import java.util.List;

public interface OrderService {

    void reserved(Order order, Long id);

    void accept(Order order);

    List<Order> findAllByTaskId(Long id);

    Order findOne(Long id);
}
