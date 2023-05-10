package com.jaideralba.internetbanking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    String name;
    Boolean exclusivePlan;
    BigDecimal balance;
    String accountNumber;
    @JsonFormat(pattern = "dd/MM/yyyy") Date birthDate;

}
