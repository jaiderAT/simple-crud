package com.jaideralba.internetbanking.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class UserEntity {

    private static final BigDecimal EXCLUSIVE_PLAN_BALANCE = new BigDecimal(100000);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    Boolean exclusivePlan = false;
    BigDecimal balance;
    String accountNumber;
    Date birthDate;

    public void calculateExclusivePlan(){
        exclusivePlan = balance.compareTo(EXCLUSIVE_PLAN_BALANCE) >= 0;
    }
}
