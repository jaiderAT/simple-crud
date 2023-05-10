package com.jaideralba.internetbanking.controller;

import com.jaideralba.internetbanking.model.User;
import com.jaideralba.internetbanking.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsersService service;

    @GetMapping
    List<User> listAll() {
        return Collections.emptyList();
    }

    @GetMapping("/{id}")
    User get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    User create(@RequestBody @Valid User user) {
        return service.create(user);
    }

    @PutMapping
    User update(@RequestBody @Valid User user) {
        return service.update(user);
    }

    @PatchMapping("/{id}") // TODO: review this
    User updatePartially(@RequestBody User user, @PathVariable Long id) {
        return new User();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id) {
    }

}
