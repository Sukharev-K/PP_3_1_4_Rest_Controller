package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
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
    public ModelAndView printUsers(
            Model model, @RequestParam(name = "id",
            required = false) Integer userId) {
        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("users", userService.printUsers());
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView newUser(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView("new");
        return modelAndView;
    }


    @PostMapping("/new")
    public ModelAndView create(@ModelAttribute("user") User user) {
        Role role = roleService.findByName("ROLE_USER");
        userService.save(user, role);
        return new ModelAndView("redirect:/admin");
    }


    @GetMapping("/edit")
    public ModelAndView edit(Model model, @RequestParam(name = "id") int id) {
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("user", userService.showUserById(id));
        return modelAndView;
    }


    @PostMapping
    public ModelAndView update(@ModelAttribute("user") User user, @RequestParam(name = "id") int id) {
        userService.save(user);
        return new ModelAndView("redirect:/admin");
    }


    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView delete(@RequestParam(name = "id") int id) {
        userService.delete(id);
        return new ModelAndView("redirect:/admin");
    }
}
