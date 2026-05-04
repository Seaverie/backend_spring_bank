package com.banking.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferResponse {
    private String transactionId;
    private String status;
    private String message;
}
