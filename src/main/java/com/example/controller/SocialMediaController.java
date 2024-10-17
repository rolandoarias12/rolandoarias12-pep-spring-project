package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

@RestController
public class SocialMediaController {
    private final MessageService messageService;
    private final AccountService accountService;

    @Autowired
    SocialMediaController(MessageService messageService, AccountService accountService){
        this.messageService = messageService;
        this.accountService = accountService;
    }

    @PostMapping("/messages")
    Message createMessage(@RequestBody Message msg){
        var hasCreated = messageService.create(msg);
        if (!hasCreated){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return msg;
    }

    @DeleteMapping("/messages/{id}")
    Integer deleteMessage(@PathVariable int id){
        var hasDeleted = messageService.delete(id);
        if (!hasDeleted){
            return null;
        }

        return 1;
    }

    @GetMapping("/accounts/{id}/messages")
    List<Message> getAllMessagesFromAccount(@PathVariable int id){
        return messageService
            .getAll(id)
            .orElseThrow(
                ()->new ResponseStatusException(HttpStatus.BAD_REQUEST)
            );
    }

    @GetMapping("/messages")
    List<Message> getAllMessages(){
        return messageService.getAll();
    }

    @GetMapping("/messages/{id}")
    Message getMessage(@PathVariable int id){
        return messageService.get(id).orElse(null);
    }

    @PatchMapping("/messages/{id}")
    Integer updateMessage(@RequestBody Message msg, @PathVariable int id){
        var hasUpdated = messageService.update(id, msg.getMessageText());
        if (!hasUpdated){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return 1;
    }

    @PostMapping("/login")
    Account login(@RequestBody Account acc){
        return accountService
            .login(acc)
            .orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED)
            );
    }

    @PostMapping("/register")
    Account register(@RequestBody Account acc){
        return accountService
            .register(acc)
            .orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.CONFLICT)
            );
    }
}
