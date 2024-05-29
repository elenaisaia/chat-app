package com.wsmt.chatapi.service;

import com.wsmt.chatapi.domain.Message;
import com.wsmt.chatapi.domain.MessageRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final EntityManager entityManager;

    @Transactional
    public Message createMessage(CreateMessageRequest request) throws Exception {
        Message message = Message.createMessage(request.getSender(), request.getText(), request.getSession());
        messageRepository.save(message);
        return message;
    }
}
