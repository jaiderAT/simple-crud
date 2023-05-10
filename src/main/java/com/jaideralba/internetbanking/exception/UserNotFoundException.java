package com.jaideralba.internetbanking.exception;

import lombok.NoArgsConstructor;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Usuário não encontrado");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
