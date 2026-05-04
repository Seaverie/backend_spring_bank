package com.banking.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$")
    private String fromAccountNumber;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$")
    private String toAccountNumber;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    private String idempotencyKey;   // optional, will be generated if not provided
}