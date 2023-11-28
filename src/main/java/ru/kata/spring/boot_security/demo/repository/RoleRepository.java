package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByNameRole(String name);
    List<Role> findAll();
}
