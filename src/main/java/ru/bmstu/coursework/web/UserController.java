package ru.bmstu.coursework.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.bmstu.coursework.domain.entities.User;
import ru.bmstu.coursework.domain.enums.Role;
import ru.bmstu.coursework.service.UserService;
import ru.bmstu.coursework.web.validators.UserValidator;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("allRoles")
    public List<Role> populateRoles() {
        return Arrays.asList(Role.values());
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new UserValidator(userService));
    }

    @GetMapping(value = {"/", "/login"})
    public ModelAndView login(@RequestParam(required = false) String message, @RequestParam(required = false) String logout) {
        ModelAndView modelAndView = new ModelAndView("index");
        if (message != null) {
            modelAndView.addObject("message", message);
        }
        if (logout != null) {
            modelAndView.addObject("message", "You've been logged out successfully");
        }
        return modelAndView;
    }

    @GetMapping(value = "/registration")
    public ModelAndView registration() {
        return new ModelAndView("registration", "user", new User());
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            return modelAndView;
        }
        userService.save(user);
        modelAndView.setViewName("redirect:/login?text=User has been registered successfully");
        return modelAndView;
    }

    @GetMapping(value = "/default")
    public String defaultAfterLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();

        String redirect;
        if (role.equals("[TEACHER]")) {
            redirect = "redirect:/teacher/home";
        } else {
            redirect = "redirect:/student/home";
        }
        return redirect;
    }
}
