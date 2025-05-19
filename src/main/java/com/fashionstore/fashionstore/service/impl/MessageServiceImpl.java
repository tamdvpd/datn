package com.fashionstore.fashionstore.service.impl;

import com.fashionstore.fashionstore.entity.Message;
import com.fashionstore.fashionstore.repository.MessageRepository;
import com.fashionstore.fashionstore.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Page<Message> getAllMessages(Pageable pageable) {
        return messageRepository.findAll(pageable);
    }

    @Override
    public Optional<Message> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    @Override
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(Integer id, Message message) {
        message.setId(id);
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(Integer id) {
        messageRepository.deleteById(id);
    }
}