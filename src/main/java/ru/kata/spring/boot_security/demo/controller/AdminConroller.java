package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.exception.DuplicateLoginException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminConroller {
    private final UserService userService;
    private final RoleService roleService;

    public AdminConroller(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public ModelAndView printUsers(@RequestParam(name = "id",
            required = false) Long userId) {
        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("users", userService.printUsers());
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView newUser(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView("new");
        modelAndView.addObject("roles", roleService.findAll());
        return modelAndView;
    }

    @PostMapping("/new")
    public ModelAndView create(@ModelAttribute("user") User user) {
        try {
            userService.save(user, user.getRoles());
            return new ModelAndView("redirect:/admin");
        } catch (DuplicateLoginException ex) {
            ModelAndView modelAndView = new ModelAndView("errorPage");
            modelAndView.addObject("errorMessage", ex.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("/edit")
    public ModelAndView edit(@RequestParam(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("user", userService.showUserById(id));
        modelAndView.addObject("roles", roleService.findAll());
        return modelAndView;
    }

    @PatchMapping
    public ModelAndView update(@ModelAttribute("user") User user, @RequestParam(name = "id") Long id) {
        userService.update(user, user.getRoles());
        return new ModelAndView("redirect:/admin");
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return new ModelAndView("redirect:/admin");
    }
}
