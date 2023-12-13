package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.kata.spring.boot_security.demo.exception.DuplicateLoginException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminControllerRest {

    private final UserService userService;
    private final RoleService roleService;

    public AdminControllerRest(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> printUsers(@RequestParam(name = "id",
            required = false) Long userId, Principal principal) {
        String user = String.valueOf(userService.findByLogin(principal.getName()));
        List<Role> roles = roleService.findAll();
        List<User> use = userService.printUsers();
        Map<String, Object> responseBody = Map.of("user", user, "roles", roles, "use", use);

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/new")
    public ResponseEntity<Map<String, Object>> newUser(@ModelAttribute("user") User user) {
        List<Role> roles = roleService.findAll();
        Map<String, Object> responseBody = Map.of("roles", roles);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/new")
    public ResponseEntity<?> create(@ModelAttribute("user") User user) {
        try {
            userService.save(user, user.getRoles());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicateLoginException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Duplicate login: " + ex.getMessage());
        }
    }

    @GetMapping("/edit")
    public ResponseEntity<Map<String, Object>> edit(@RequestParam(name = "id") Long id) {
        User user = userService.showUserById(id);
        List<Role> roles = roleService.findAll();
        Map<String, Object> responseBody = Map.of("user", user, "roles", roles);
        return ResponseEntity.ok(responseBody);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> update(@ModelAttribute("user") User user,
                               @PathVariable("id") Long id) {
        userService.update(user, user.getRoles());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}