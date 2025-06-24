package br.com.realtour.service;

import br.com.realtour.entity.*;
import br.com.realtour.repository.ChatRepository;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.repository.RealtorRepository;
import br.com.realtour.repository.UnitRepository; // Import UnitRepository
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        log.info(token + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+clientEmail);

        return clientRepository.findByEmail(clientEmail)
                .switchIfEmpty(Mono.error(new RuntimeException("Only clients can initiate a chat.")))
                .flatMap(client -> {
                    Mono<Realtor> realtorMono = realtorRepository.findByEmail(realtorEmail);
                    Mono<Unit> unitMono = unitRepository.findById(unitId);

                    return Mono.zip(realtorMono, unitMono)
                            .flatMap(tuple -> {
                                Realtor realtor = tuple.getT1();
                                Unit unit = tuple.getT2();

                                if (realtor == null) return Mono.error(new RuntimeException("Realtor not found."));
                                if (unit == null) return Mono.error(new RuntimeException("Unit not found."));


                                return chatRepository.findByClientAndRealtorAndUnit(client, realtor, unit)
                                        .switchIfEmpty(Mono.defer(() -> {
                                            Chat newChat = Chat.builder()
                                                    .client(client)
                                                    .realtor(realtor)
                                                    .unit(unit)
                                                    .messages(new ArrayList<>())
                                                    .build();
                                            return chatRepository.save(newChat);
                                        }));
                            });
                });
    }

    public Mono<Chat> sendMessage(Message message, String chatId) {
        return chatRepository.findById(chatId)
                .switchIfEmpty(Mono.error(new RuntimeException("Chat not found")))
                .flatMap(chat -> {
                    message.setTimestamp(LocalDateTime.now());
                    chat.getMessages().add(message);
                    return chatRepository.save(chat);
                });
    }
}