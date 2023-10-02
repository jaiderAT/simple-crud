package com.jaideralba.internetbanking.exception;

public class DuplicatedAccountException extends RuntimeException {

    public DuplicatedAccountException() {
        super("Número de conta corrente já cadastrado");
    }

    public DuplicatedAccountException(String message) {
        super(message);
    }
}
