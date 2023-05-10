package com.jaideralba.internetbanking.model.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ValidationError extends CustomError {

    public ValidationError(String title, int status, String details, List<ValidationErrorDetails> validationErrors) {
        super(title, status, details);
        this.validationErrors = validationErrors;
    }

    @Schema(description = "Lista de campos onde ocorreram erros de validação")
    List<ValidationErrorDetails> validationErrors;
}
