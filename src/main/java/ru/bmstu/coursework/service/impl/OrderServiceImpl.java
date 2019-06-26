package ru.bmstu.coursework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bmstu.coursework.domain.entities.Order;
import ru.bmstu.coursework.domain.entities.Task;
import ru.bmstu.coursework.repository.OrderRepository;
import ru.bmstu.coursework.service.OrderService;
import ru.bmstu.coursework.service.TaskService;
import ru.bmstu.coursework.service.UserService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final TaskService taskService;

    private final UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, TaskService taskService, UserService userService) {
        this.orderRepository = orderRepository;
        this.taskService = taskService;
        this.userService = userService;
    }

    @Override
    public void reserved(Order order, Long id) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        order.setTask(task);
        order.setUser(userService.getCurrentUser());
        orderRepository.save(order);
    }

    @Override
    public void accept(Order order) {
        order.setAccept(true);
        orderRepository.save(order);
    }

    @Override
    public List<Order> findAllByTaskId(Long id) {
        return orderRepository.findAllByTaskId(id);
    }

    @Override
    public Order findOne(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid orders Id:" + id));
    }
}
