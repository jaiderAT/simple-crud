package com.jaideralba.internetbanking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @NotEmpty
    @Size(min=3, max=200)
    String name;
    @NotNull
    Boolean exclusivePlan;
    @NotNull
    @Min(0)
    @Max(999999999)
    BigDecimal balance;
    @NotEmpty
    @Pattern(regexp = "^[0-9]{6}\\-[0-9]$", message = "Formato inválido. Deve conter número da conta com 6 digitos, mais o número da agência separado por hífen. (ex: 123456-7)")
    String accountNumber;
    @JsonFormat(pattern = "dd/MM/yyyy", lenient = OptBoolean.FALSE)
    @NotNull
    Date birthDate;

}
