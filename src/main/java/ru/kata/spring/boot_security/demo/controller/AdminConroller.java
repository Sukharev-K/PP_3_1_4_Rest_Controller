package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
@RequestMapping("/admin")
public class AdminConroller {
    private final UserService userService;


    public AdminConroller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String printUsers(
            Model model, @RequestParam(name = "id",
            required = false) Integer userId) {
        model.addAttribute("users", userService.printUsers());
        return "users";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam(name = "id") int id) {
        model.addAttribute("user", userService.showUserById(id));
        return "edit";
    }

    @PostMapping
    public String update(@ModelAttribute("user") User user, @RequestParam(name = "id") int id) {
        userService.save(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String delete(@RequestParam(name = "id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
