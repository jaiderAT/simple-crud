package com.jaideralba.internetbanking.model.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomError {
    @Schema(description = "Mensagem de erro")
    String title;
    @Schema(description = "Código HTTP de retorno do erro")
    int status;
    @Schema(description = "Mensagem detalhada do erro")
    String details;
}
