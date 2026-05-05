package com.banking.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AccountRequest {
    @NotBlank
    private String currency;
    @PositiveOrZero
    private int amount;
}
