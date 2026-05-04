package com.banking.demo.repository;

import com.banking.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByUserId(Long userId);

    @Query("SELECT a FROM Account a WHERE a.user.username = :username")
    List<Account> findAccountsByUsername(@Param("username") String username);

}
