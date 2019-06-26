package ru.bmstu.coursework.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.bmstu.coursework.domain.entities.Message;
import ru.bmstu.coursework.domain.entities.Order;
import ru.bmstu.coursework.service.MessageService;
import ru.bmstu.coursework.service.OrderService;
import ru.bmstu.coursework.service.TaskService;

import javax.validation.Valid;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final TaskService taskService;

    private final MessageService messageService;

    private final OrderService orderService;

    @Autowired
    public TeacherController(TaskService taskService, MessageService messageService, OrderService orderService) {
        this.taskService = taskService;
        this.messageService = messageService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/home")
    public ModelAndView home(Model model) {
        model.addAttribute("taskList", taskService.findAll());
        return new ModelAndView("teacher/home");
    }

    @GetMapping(value = "/home/{id}")
    public ModelAndView showTask(@PathVariable("id") Long id, Model model) {
        model.addAttribute("task", taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id)));
        model.addAttribute("messages", messageService.findAllByTaskId(id));
        return new ModelAndView("teacher/get-task", "message", new Message());
    }

    @PostMapping(value = "/home/{id}/create")
    public ModelAndView createMessage(@PathVariable("id") Long id, @Valid Message message, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            return modelAndView;
        }
        messageService.send(message, id);
        modelAndView.setViewName("redirect:/teacher/home/" + id);
        return modelAndView;
    }

    @GetMapping(value = "/home/{id}/order")
    public ModelAndView showOffer(@PathVariable("id") Long id, Model model) {
        model.addAttribute("task", taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id)));
        model.addAttribute("messages", messageService.findAllByTaskId(id));
        return new ModelAndView("teacher/order", "order", new Order());
    }

    @PostMapping(value = "/home/{id}/order/create")
    public ModelAndView createReserve(@PathVariable("id") Long id, @Valid Order order, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/teacher/home/" + id + "/orders");
            return modelAndView;
        }
        orderService.reserved(order, id);
        modelAndView.setViewName("redirect:/teacher/home/" + id);
        return modelAndView;
    }

}
