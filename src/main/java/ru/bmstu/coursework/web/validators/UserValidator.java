package ru.bmstu.coursework.web.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.bmstu.coursework.domain.entities.User;
import ru.bmstu.coursework.service.UserService;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            errors.rejectValue("email", "", "There is already a user registered with the email provided");
        }
    }

}
