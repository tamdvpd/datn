package com.fashionstore.fashionstore.controller;

import com.fashionstore.fashionstore.entity.Message;
import com.fashionstore.fashionstore.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<Message>> getAll() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getById(@PathVariable Integer id) {
        return ResponseEntity.of(messageService.getMessageById(id));
    }

    @PostMapping
    public ResponseEntity<Message> create(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.createMessage(message));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> update(@PathVariable Integer id, @RequestBody Message message) {
        return ResponseEntity.ok(messageService.updateMessage(id, message));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}