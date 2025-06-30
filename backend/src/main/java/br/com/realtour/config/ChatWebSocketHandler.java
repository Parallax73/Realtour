package br.com.realtour.config;

import br.com.realtour.entity.Chat;
import br.com.realtour.entity.Message;
import br.com.realtour.repository.ChatRepository;
import br.com.realtour.service.ChatService;
import br.com.realtour.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler implements WebSocketHandler {

    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // Map to store active sessions for each chat
    private final Map<String, Set<WebSocketSession>> chatSessions = new ConcurrentHashMap<>();

    @Override
    public @NotNull Mono<Void> handle(WebSocketSession session) {
        ConnectionParams params = extractConnectionParams(session);
        if (!params.isValid()) {
            log.error("WebSocket closing: Missing token or chatId. token={}, chatId={}", params.token(), params.chatId());
            return session.close();
        }

        Mono<String> emailMono = extractEmailFromToken(params.token());
        if (emailMono == null) {
            return session.close();
        }

        return handleWebSocketConnection(session, params.chatId(), emailMono);
    }

    private record ConnectionParams(String chatId, String token) {
        boolean isValid() {
            return chatId != null && token != null;
        }
    }

    private ConnectionParams extractConnectionParams(WebSocketSession session) {
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

        log.info("WebSocket connection attempt: chatId={}, tokenPresent={}", chatId, token != null);
        return new ConnectionParams(chatId, token);
    }

    private Mono<String> extractEmailFromToken(String token) {
        try {
            return Mono.just(jwtService.extractEmail(token));
        } catch (Exception e) {
            log.error("WebSocket closing: Failed to extract email from JWT. Exception: {}", e.getMessage(), e);
            return null;
        }
    }

    private Mono<Void> handleWebSocketConnection(WebSocketSession session, String chatId, Mono<String> emailMono) {
        return emailMono
                .flatMap(email -> chatRepository.findById(chatId)
                        .flatMap(chat -> {
                            if (!isParticipant(chat, email)) {
                                log.error("WebSocket closing: User {} is not a participant of chatId={}", email, chatId);
                                return session.close();
                            }

                            // Add session to chat sessions
                            addSessionToChat(chatId, session);

                            // Send initial chat history
                            sendChatHistory(session, chat);

                            // Handle incoming messages
                            Flux<WebSocketMessage> incoming = handleIncomingMessages(session, chatId, email);

                            return session.send(incoming)
                                    .doFinally(signalType -> removeSessionFromChat(chatId, session));
                        })
                        .switchIfEmpty(Mono.defer(() -> {
                            log.error("WebSocket closing: Chat not found for chatId={}", chatId);
                            return session.close();
                        })))
                .doOnError(e -> log.error("WebSocket closing: Unexpected error", e));
    }

    private boolean isParticipant(Chat chat, String email) {
        return (chat.getClientEmail() != null && chat.getClientEmail().equals(email))
                || (chat.getRealtorEmail() != null && chat.getRealtorEmail().equals(email));
    }

    private void addSessionToChat(String chatId, WebSocketSession session) {
        chatSessions.computeIfAbsent(chatId, k -> new CopyOnWriteArraySet<>()).add(session);
    }

    private void removeSessionFromChat(String chatId, WebSocketSession session) {
        Set<WebSocketSession> sessions = chatSessions.get(chatId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                chatSessions.remove(chatId);
            }
        }
    }

    private void sendChatHistory(WebSocketSession session, Chat chat) {
        try {
            String chatJson = objectMapper.writeValueAsString(chat);
            session.send(Mono.just(session.textMessage(chatJson))).subscribe();
        } catch (Exception e) {
            log.error("Could not send chat history: {}", e.getMessage(), e);
        }
    }

    private Flux<WebSocketMessage> handleIncomingMessages(WebSocketSession session, String chatId, String email) {
        return session.receive()
                .flatMap(msg -> {
                    try {
                        Message message = objectMapper.readValue(msg.getPayloadAsText(), Message.class);
                        message.setSender(email);
                        return chatService.sendMessage(message, chatId)
                                .flatMap(updatedChat -> broadcastToAllSessions(chatId, updatedChat));
                    } catch (Exception e) {
                        log.error("Failed to process message: {}", e.getMessage(), e);
                        return Mono.empty();
                    }
                });
    }

    private Mono<WebSocketMessage> broadcastToAllSessions(String chatId, Chat updatedChat) {
        try {
            String json = objectMapper.writeValueAsString(updatedChat);
            Set<WebSocketSession> sessions = chatSessions.get(chatId);
            if (sessions != null) {
                sessions.removeIf(s -> !s.isOpen());
                sessions.forEach(s ->
                        s.send(Mono.just(s.textMessage(json))).subscribe()
                );
            }
            return Mono.empty();
        } catch (Exception e) {
            log.error("Failed to broadcast message: {}", e.getMessage(), e);
            return Mono.empty();
        }
    }
}