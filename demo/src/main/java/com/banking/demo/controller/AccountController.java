package com.banking.demo.controller;

import com.banking.demo.dto.AccountRequest;
import com.banking.demo.entity.Account;
import com.banking.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/search/{accountNumber}")
    public Account getAccount(@PathVariable String accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }
    @PostMapping("/create")
    public Account createAccount(@RequestBody AccountRequest account) {
        return accountService.createAccount(account);
    }
}
