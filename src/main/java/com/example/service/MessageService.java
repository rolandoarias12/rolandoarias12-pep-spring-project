package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public boolean create(Message msg){
        if (!accountRepository.existsById(msg.getPostedBy())){
            return false;
        }
        if (!textIsValid(msg.getMessageText())){
            return false;
        }

        messageRepository.save(msg);
        return true;
    }

    public boolean delete(int id){
        if (!messageRepository.existsById(id)){
            return false;
        }
        
        messageRepository.deleteById(id);
        return true;
    }

    public Optional<List<Message>> getAll(int accountId){
        if (!accountRepository.existsById(accountId)){
            return Optional.empty();
        }

        return Optional.of(
            messageRepository.findByPostedBy(accountId)
        );
    }

    public List<Message> getAll(){
        return messageRepository.findAll();
    }

    public Optional<Message> get(int messageId){
        return messageRepository.findById(messageId);
    }

    public boolean update(int id, String newText){
        if (!messageRepository.existsById(id)){
            return false;
        }
        if (!textIsValid(newText)){
            return false;
        }

        var oldMsg = messageRepository.getById(id);
        oldMsg.setMessageText(newText);
        messageRepository.save(oldMsg);

        return true;
    }

    private boolean textIsValid(String text){
        if (text.length() == 0){
            return false;
        }
        if (text.length() > 255){
            return false;
        }
        return true;
    }
}
