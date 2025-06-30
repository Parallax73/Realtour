package br.com.realtour.controller;

import br.com.realtour.entity.Chat;
import br.com.realtour.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

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
        String unitId = "685efdda607d1c5477d1b15b";
        String realtorUsername = "papa1@gmail.com";
        return chatService.createChat(token, unitId, realtorUsername)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error creating chat: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    @GetMapping("/get-chats")
    public Mono<ResponseEntity<List<Chat>>> getChats(@CookieValue("SecureJWT") String token) {
        log.info("Received request to get chats with token: {}", token);
        return chatService.getUserChats(token)
                .collectList()
                .map(chats -> {
                    log.info("Retrieved {} chats", chats.size());
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(chats);
                })
                .onErrorResume(e -> {
                    log.error("Failed to fetch chats: {}", e.getMessage(), e);
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                });
    }

}
