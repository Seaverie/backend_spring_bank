package com.banking.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QrResquest {
    @NotBlank
    @Pattern(regexp = "^[0-9]{9,12}$")
    private String accountNumber;
    private BigDecimal amount;
}
