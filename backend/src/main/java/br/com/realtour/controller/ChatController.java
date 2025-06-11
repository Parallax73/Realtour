package br.com.realtour.controller;

import br.com.realtour.entity.Chat;
import br.com.realtour.entity.Message;
import br.com.realtour.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/chats")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatService chatService;

    @PostMapping
    public Mono<ResponseEntity<Chat>> createChat(
            @RequestHeader("Authorization") String token,
            @RequestParam String unitId,
            @RequestParam String realtorUsername) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        return chatService.createChat(token, unitId, realtorUsername)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error creating chat: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    // ... inside your ChatController class

    @MessageMapping("/chat/{chatId}/sendMessage")
    public void sendMessage(@DestinationVariable String chatId, @Payload Message message, Principal principal) {
        message.setSenderUsername(principal.getName());
        
        chatService.sendMessage(message, chatId).subscribe(chat -> {
            messagingTemplate.convertAndSend("/topic/chat/" + chatId, chat);
        });
    }
}