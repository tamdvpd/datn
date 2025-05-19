package com.fashionstore.fashionstore.service;

import com.fashionstore.fashionstore.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    List<Message> getAllMessages();
    Page<Message> getAllMessages(Pageable pageable);
    Optional<Message> getMessageById(Integer id);
    Message createMessage(Message message);
    Message updateMessage(Integer id, Message message);
    void deleteMessage(Integer id);
}