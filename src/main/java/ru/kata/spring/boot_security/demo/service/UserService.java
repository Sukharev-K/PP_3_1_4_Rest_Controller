package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> printUsers();
    User showUserById(Long id);
    void save(User user, Set<Role> role);
    void update(User user, Set<Role> roles);
    void delete(Long id);
    User findByLogin(String login);
}

