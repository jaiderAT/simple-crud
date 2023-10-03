package com.jaideralba.internetbanking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @Schema(description = "Número identificador do usuário", example = "1234")
    Long id;

    @Schema(description = "Nome do titular da conta bancária", example = "Marco Antonio")
    @NotEmpty
    @Size(min=3, max=200)
    String name;

    @Schema(description = "Saldo na conta bancária", example = "5480.55")
    @NotNull
    @Digits(integer=9, fraction=2)
    BigDecimal balance;

    @Schema(description = "Número da conta bancária")
    @NotEmpty
    @Pattern(regexp = "^[0-9]{6}\\-[0-9]$", message = "Formato inválido. Deve conter número da conta com 6 digitos, mais o número da agência separado por hífen. (ex: 123456-7)")
    String accountNumber;

    @Schema(type="string", description = "Data de nascimento do titular da conta bancária", example = "22/07/1988")
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull
    LocalDate birthDate;

}
