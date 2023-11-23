package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> printUsers();
    User showUserById(Integer id);
    void save(User user);
    void delete(Integer id);
    User findByLogin(String login);
}

