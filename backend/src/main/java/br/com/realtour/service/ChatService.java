package br.com.realtour.service;

import br.com.realtour.entity.Chat;
import br.com.realtour.entity.Client;
import br.com.realtour.entity.Message;
import br.com.realtour.entity.Realtor;
import br.com.realtour.entity.Unit;
import br.com.realtour.repository.ChatRepository;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.repository.RealtorRepository;
import br.com.realtour.repository.UnitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@Slf4j
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private RealtorRepository realtorRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private JwtService jwtService;

    public Mono<Chat> createChat(String token, String unitId, String realtorEmail) {
        String clientEmail = jwtService.extractEmail(token);

        return clientRepository.findByEmail(clientEmail)
                .switchIfEmpty(Mono.error(new RuntimeException("Only clients can initiate a chat.")))
                .flatMap(client ->
                        realtorRepository.findByEmail(realtorEmail)
                                .switchIfEmpty(Mono.error(new RuntimeException("Realtor not found.")))
                                .flatMap(realtor ->
                                        unitRepository.findById(unitId)
                                                .switchIfEmpty(Mono.error(new RuntimeException("Unit not found.")))
                                                .flatMap(unit ->
                                                        chatRepository.findByClientEmailAndRealtorEmailAndUnitId(
                                                                        client.getEmail(), realtor.getEmail(), unit.getId())
                                                                .switchIfEmpty(Mono.defer(() -> {
                                                                    Chat newChat = Chat.builder()
                                                                            .clientEmail(client.getEmail())
                                                                            .clientName(client.getUsername())
                                                                            .realtorEmail(realtor.getEmail())
                                                                            .realtorName(realtor.getUsername())
                                                                            .unitId(unit.getId())
                                                                            .unitAddress(unit.getAddress())   // or whatever the field is called
                                                                            .unitNumber(unit.getNumber())     // or whatever the field is called
                                                                            .messages(new ArrayList<>())
                                                                            .build();
                                                                    return chatRepository.save(newChat);
                                                                }))
                                                )
                                )
                );
    }

    public Mono<Chat> sendMessage(Message message, String chatId) {
        return chatRepository.findById(chatId)
                .switchIfEmpty(Mono.error(new RuntimeException("Chat not found")))
                .flatMap(chat -> {
                    message.setTimestamp(LocalDateTime.now());
                    message.setType(Message.MessageType.CHAT);
                    chat.getMessages().add(message);
                    return chatRepository.save(chat);
                });
    }

    public Flux<Chat> getUserChats(String token) {
        String email = jwtService.extractEmail(token);
        return chatRepository.findByClientEmailOrRealtorEmail(email, email);
    }
}