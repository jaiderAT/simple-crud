package com.jaideralba.internetbanking.model.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ValidationErrorDetails {
    @Schema(description = "Campo que ocorreu o erro de validação")
    String field;
    @Schema(description = "Descrição do erro de validação")
    String message;
}
