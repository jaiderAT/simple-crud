package com.jaideralba.internetbanking.model.error;

import lombok.Data;

import java.util.List;

@Data
public class ValidationError extends CustomError {

    public ValidationError(String title, int status, String details, List<ValidationErrorDetails> validationErrors) {
        super(title, status, details);
        this.validationErrors = validationErrors;
    }

    List<ValidationErrorDetails> validationErrors;
}
