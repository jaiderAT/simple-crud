package com.jaideralba.internetbanking.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends UserRequest {

    @Schema(description = "Identificador booleano se a conta possui 'Plano Exclusivo'", example = "true")
    @NotNull
    Boolean exclusivePlan;

}
