package com.jaideralba.internetbanking.controller;

import com.jaideralba.internetbanking.model.UserRequest;
import com.jaideralba.internetbanking.model.UserResponse;
import com.jaideralba.internetbanking.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService service;

    @GetMapping
    List<UserResponse> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca e retorna as informações do usuário pelo ID")
    UserResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria novo usuário")
    UserResponse create(@RequestBody @Valid UserRequest user) {
        return service.create(user);
    }

    @PutMapping
    @Operation(summary = "Atualiza dados de usuário existente")
    UserResponse update(@RequestBody @Valid UserRequest user) {
        return service.update(user);
    }

    @PatchMapping("/{id}")
    UserResponse updatePartial(@RequestBody UserRequest user, @PathVariable Long id) {
        user.setId(id);
        return service.updatePartially(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui usuário pelo ID")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
