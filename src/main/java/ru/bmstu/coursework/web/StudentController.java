package ru.bmstu.coursework.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.bmstu.coursework.domain.entities.Message;
import ru.bmstu.coursework.domain.entities.Order;
import ru.bmstu.coursework.domain.entities.Task;
import ru.bmstu.coursework.domain.enums.Category;
import ru.bmstu.coursework.service.MessageService;
import ru.bmstu.coursework.service.OrderService;
import ru.bmstu.coursework.service.TaskService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final TaskService taskService;

    private final MessageService messageService;

    private final OrderService orderService;

    @Autowired
    public StudentController(TaskService taskService, MessageService messageService, OrderService orderService) {
        this.taskService = taskService;
        this.messageService = messageService;
        this.orderService = orderService;
    }

    @ModelAttribute("allCategories")
    public List<Category> populateCategories() {
        return Arrays.asList(Category.values());
    }

    @GetMapping(value = "/home")
    public ModelAndView home(@RequestParam(required = false) String message, Model model) {
        if (message != null) {
            model.addAttribute("message", message);
        }
        model.addAttribute("taskList", taskService.findAllStudentTasks());
        return new ModelAndView("student/home");
    }

    @GetMapping(value = "/home/{id}")
    public ModelAndView showTask(@PathVariable("id") Long id, @RequestParam(required = false) String message, Model model) {
        if (message != null) {
            model.addAttribute("mes", message);
        }
        model.addAttribute("task", taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id)));
        model.addAttribute("messages", messageService.findAllByTaskId(id));
        return new ModelAndView("student/get-task", "message", new Message());
    }

    @GetMapping(value = "/add")
    public ModelAndView showCreateTask() {
        return new ModelAndView("student/add-task", "task", new Task());
    }

    @PostMapping(value = "/create")
    public ModelAndView createTask(@Valid Task task, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("student/add-task");
            return modelAndView;
        }
        taskService.save(task);
        modelAndView.setViewName("redirect:/student/home?text=Task has been created successfully");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateTask(@PathVariable("id") Long id, Model model) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));

        model.addAttribute("task", task);
        return new ModelAndView("student/update-task");
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateTask(@PathVariable("id") Long id, @Valid Task task, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            task.setId(id);
            modelAndView.setViewName("student/update-task");
            return modelAndView;
        }

        taskService.save(task);
        modelAndView.setViewName("redirect:/student/home?text=Task has been updated successfully");
        return modelAndView;
    }

    @PostMapping(value = "/home/{id}/create")
    public ModelAndView createMessage(@PathVariable("id") Long id, @Valid Message message, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/student/home/" + id);
            return modelAndView;
        }
        messageService.send(message, id);
        modelAndView.setViewName("redirect:/student/home/" + id);
        return modelAndView;
    }

    @GetMapping(value = "/home/{id}/order")
    public ModelAndView showOffer(@PathVariable("id") Long id, Model model) {
        model.addAttribute("task", taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id)));
        model.addAttribute("messages", messageService.findAllByTaskId(id));
        model.addAttribute("allOffers", orderService.findAllByTaskId(id));
        return new ModelAndView("student/order");
    }

    @GetMapping(value = "/home/{id}/order/create/{orderId}")
    public ModelAndView createMessage(@PathVariable Long id, @PathVariable Long orderId) {
        ModelAndView modelAndView = new ModelAndView();
        Order order = orderService.findOne(orderId);
        orderService.accept(order);
        modelAndView.setViewName("redirect:/student/home/" + id + "?messages=You have been successfully accept the offer");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteTask(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        Task task = taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        taskService.delete(task);
        modelAndView.setViewName("redirect:/student/home?text=Task has been deleted successfully");
        return modelAndView;
    }

}
