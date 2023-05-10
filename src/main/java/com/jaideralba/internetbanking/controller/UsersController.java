package com.jaideralba.internetbanking.controller;

import com.jaideralba.internetbanking.model.User;
import com.jaideralba.internetbanking.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService service;

    @GetMapping
    List<User> listAll() {
        return Collections.emptyList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca e retorna as informações do usuário pelo ID")
    User get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria novo usuário")
    User create(@RequestBody @Valid User user) {
        return service.create(user);
    }

    @PutMapping
    @Operation(summary = "Atualiza dados de usuário existente")
    User update(@RequestBody @Valid User user) {
        return service.update(user);
    }

    @PatchMapping("/{id}") // TODO: review this
    User updatePartially(@RequestBody User user, @PathVariable Long id) {
        return new User();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui usuário pelo ID")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
