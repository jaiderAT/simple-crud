package com.jaideralba.internetbanking.exception;

public class InvalidUserIdException extends RuntimeException {

    public InvalidUserIdException() {
        super("ID do usuário inválido");
    }

    public InvalidUserIdException(String message) {
        super(message);
    }
}
