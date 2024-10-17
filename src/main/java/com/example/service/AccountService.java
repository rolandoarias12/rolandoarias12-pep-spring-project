package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Optional<Account> login(Account acc){
        return accountRepository
            .findByUsername(
                acc.getUsername()
            )
            .flatMap(
                it->{
                    if (!it.getPassword().equals(acc.getPassword())){
                        return Optional.empty();
                    }
                    return Optional.of(it);
                }
            );
    }

    public Optional<Account> register(Account acc){
        if (accountRepository.findByUsername(acc.getUsername()).isPresent()){
            return Optional.empty();
        }

        accountRepository.save(acc);

        return Optional.of(acc);
    }
}
