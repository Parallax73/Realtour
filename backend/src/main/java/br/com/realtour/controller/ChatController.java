package br.com.realtour.controller;

import br.com.realtour.entity.Chat;
import br.com.realtour.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/chats")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/create")
    public Mono<ResponseEntity<Chat>> createChat(
            @CookieValue("SecureJWT") String token) {

        String unitId = "685ad63c97196f6aa86d3530";
        String realtorUsername = "papa1@gmail.com";
        return chatService.createChat(token, unitId, realtorUsername)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error creating chat: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }
}
