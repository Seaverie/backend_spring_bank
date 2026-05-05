package com.banking.demo.service;


import com.banking.demo.dto.AccountRequest;
import com.banking.demo.entity.Account;
import com.banking.demo.entity.User;
import com.banking.demo.repository.AccountRepository;
import com.banking.demo.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final SecureRandom random = new SecureRandom();
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private String generateUniqueAccountNumber() {
        int maxAttempts = 10;
        for (int i = 0; i < maxAttempts; i++) {
            long number = 1_000_000_000L + (long)(random.nextDouble() * 9_000_000_000L);
            String accountNumber = String.valueOf(number);

            // Ensure exactly 10 digits (should always be, but safety)
            if (accountNumber.length() < 10) {
                accountNumber = "1" + "0".repeat(9 - accountNumber.length()) + accountNumber;
            }

            // Check uniqueness
            if (!accountRepository.existsByAccountNumber(accountNumber)) {
                return accountNumber;
            }
        }
        throw new RuntimeException("Unable to generate unique account number after " + maxAttempts + " attempts");
    }

    @Cacheable(value = "accounts", key = "#accountNumber")
    public Account getAccountByNumber(String accountNumber) {
        // Simulate slow DB call
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @CacheEvict(value = "accounts", key = "#account.accountNumber")
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }
    @Transactional
    public Account createAccount(AccountRequest request) {
        log.info("Starting account creation");
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("Username: {}", username);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            log.info("User found: {}", user.getUsername());

            String accountNumber = generateUniqueAccountNumber();
            log.info("Generated account number: {}", accountNumber);

            Account account = Account.builder()
                    .accountNumber(accountNumber)
                    .balance(request.getAmount() != 0 ? BigDecimal.valueOf(request.getAmount()) : BigDecimal.ZERO)
                    .currency(request.getCurrency().toUpperCase())
                    .isActive(true)
                    .user(user)
                    .build();

            log.info("Saving account...");
            Account saved = accountRepository.save(account);
            log.info("Account saved with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Account creation failed", e);
            throw new RuntimeException("Unable to create account", e);
        }
    }

    // Add method to check existence (used inside generator)
    public boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }
}
