package com.jaideralba.internetbanking.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserResponse extends UserRequest {

    public UserResponse(Long id, String name, BigDecimal balance, String accountNumber, LocalDate birthDate, Boolean exclusivePlan) {
        super(id, name, balance, accountNumber, birthDate);
        this.exclusivePlan = exclusivePlan;
    }

    @Schema(description = "Identificador booleano se a conta possui 'Plano Exclusivo'", example = "true")
    @NotNull
    Boolean exclusivePlan;

}
