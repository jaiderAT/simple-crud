package com.jaideralba.internetbanking.model.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ValidationErrorDetails {
    String field;
    String message;
}
