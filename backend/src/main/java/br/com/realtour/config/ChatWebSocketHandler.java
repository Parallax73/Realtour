package br.com.realtour.config;

import br.com.realtour.entity.Message;
import br.com.realtour.service.ChatService;
import br.com.realtour.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler implements WebSocketHandler {

    private final ChatService chatService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public @NotNull Mono<Void> handle(WebSocketSession session) {
        String query = session.getHandshakeInfo().getUri().getQuery();
        String chatId = null;
        String token = null;
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("chatId")) chatId = pair[1];
                    else if (pair[0].equals("token")) token = pair[1];
                }
            }
        }
        if (token == null || chatId == null) {
            return session.close();
        }
        String email;
        try {
            email = jwtService.extractEmail(token);
        } catch (Exception e) {
            return session.close();
        }

        String finalChatId = chatId;
        Flux<WebSocketMessage> incoming = session.receive()
                .flatMap(msg -> {
                    try {
                        Message message = objectMapper.readValue(msg.getPayloadAsText(), Message.class);
                        message.setSenderUsername(email);
                        return chatService.sendMessage(message, finalChatId)
                                .map(updatedChat -> {
                                    String json;
                                    try {
                                        json = objectMapper.writeValueAsString(updatedChat);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    return session.textMessage(json);
                                });
                    } catch (Exception e) {
                        return Mono.empty();
                    }
                });

        return session.send(incoming);
    }
}