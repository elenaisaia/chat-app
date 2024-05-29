package com.wsmt.chatapi.api;

import com.wsmt.chatapi.domain.Message;
import com.wsmt.chatapi.service.CreateMessageRequest;
import com.wsmt.chatapi.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin
public class MessageController {
    private final MessageService messageService;

    @PostMapping(path = "/messages", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createMessage(@RequestBody CreateMessageRequest request) {
        try {
            Message response = messageService.createMessage(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Sender and text can not be null! :<");
        }
    }
}
