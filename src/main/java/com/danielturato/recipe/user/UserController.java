package com.danielturato.recipe.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class UserController {

    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(path = "/sign-up", method = RequestMethod.GET)
    public String signUp(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(path = "/sign-up", method = RequestMethod.POST)
    public String newUser(@Valid User user) {
        user.setRoles(new String[]{"ROLE_USER"});
        userService.save(user);

        return "redirect:/login";
    }

    @RequestMapping(path = "/users/{username}", method = RequestMethod.GET)
    public String profile(@PathVariable String username, Model model) {
        model.addAttribute("user", userService.findByUsername(username));
        model.addAttribute("favs", getUser()
                .getFavorites());
        return "profile";
    }

    private User getUser() {
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
