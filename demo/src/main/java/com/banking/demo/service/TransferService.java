package com.banking.demo.service;

import com.banking.demo.dto.TransferRequest;
import com.banking.demo.dto.TransferResponse;
import com.banking.demo.entity.Account;
import com.banking.demo.entity.Transaction;
import com.banking.demo.repository.AccountRepository;
import com.banking.demo.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final IdempotencyService idempotencyService;

    @Transactional(rollbackOn = Exception.class)
    public TransferResponse transferMoney(TransferRequest request) {
        // 1. Idempotency check
        String idempotencyKey = request.getIdempotencyKey();
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            idempotencyKey = UUID.randomUUID().toString();
        }

        if (idempotencyService.isOperationProcessed(idempotencyKey)) {
            log.warn("Duplicate request for key: {}", idempotencyKey);
            throw new RuntimeException("Duplicate transaction request");
        }

        // 2. Fetch accounts
        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        // 3. Validate balance
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 4. Perform transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // 5. Record transaction
        Transaction transaction = Transaction.builder()
                .fromAccountNumber(request.getFromAccountNumber())
                .toAccountNumber(request.getToAccountNumber())
                .amount(request.getAmount())
                .status("SUCCESS")
                .idempotencyKey(idempotencyKey)
                .build();
        transactionRepository.save(transaction);

        // 6. Store idempotency key
        idempotencyService.storeOperationKey(idempotencyKey);

        return TransferResponse.builder()
                .transactionId(transaction.getId().toString())
                .status("SUCCESS")
                .message("Transfer completed")
                .build();
    }
}
