package com.jaideralba.internetbanking.handler;

import com.jaideralba.internetbanking.exception.InvalidUserIdException;
import com.jaideralba.internetbanking.exception.UserNotFoundException;
import com.jaideralba.internetbanking.model.error.CustomError;
import com.jaideralba.internetbanking.model.error.ValidationError;
import com.jaideralba.internetbanking.model.error.ValidationErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception exception){
        return new ResponseEntity<>(new CustomError("Erro inesperado", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidUserIdException.class, UserNotFoundException.class})
    public ResponseEntity<?> handleUserNotFoundException(Exception exception){
        return new ResponseEntity<>(new CustomError("Usuário não encontrado", HttpStatus.NOT_FOUND.value(),
                exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException exception){
        return new ResponseEntity<>(getValidationErrors(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception){
        return new ResponseEntity<>(new CustomError("Erro inesperado ao realizar parse da requisição",
                HttpStatus.BAD_REQUEST.value(), exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private static ValidationError getValidationErrors(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        List<ValidationErrorDetails> validationErrors = fieldErrors.stream()
                .map(error -> new ValidationErrorDetails(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ValidationError("Erro de validação", HttpStatus.BAD_REQUEST.value(),
                "Campo(s) em formato inválido", validationErrors);
    }
}
