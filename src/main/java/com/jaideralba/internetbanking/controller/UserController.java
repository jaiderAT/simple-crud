package com.jaideralba.internetbanking.controller;

import com.jaideralba.internetbanking.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    List<User> listAll() {
        return Collections.emptyList();
    }

    @GetMapping("/{id}")
    User getFromId(@PathVariable String id) {
        return new User();
    }

    @PostMapping
    User create(@RequestBody User user) {
        return new User();
    }

    @PutMapping("/{id}")
    User update(@RequestBody User user, @PathVariable String id) {
        return new User();
    }

    @PatchMapping("/{id}") // TODO: review this
    User updatePartially(@RequestBody User user, @PathVariable String id) {
        return new User();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable String id) {
    }


}
