package br.com.realtour.service;

import br.com.realtour.entity.Chat;
import br.com.realtour.entity.Client;
import br.com.realtour.entity.Message;
import br.com.realtour.entity.Realtor;
import br.com.realtour.repository.ChatRepository;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.repository.RealtorRepository;
import br.com.realtour.util.Sender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;

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
    private JwtService service;

    public Mono<Chat> createChat(String authHeader, String realtorUsername) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        String username = service.extractUsername(token);

        Mono<Client> clientMono = clientRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Client not found")));

        Mono<Realtor> realtorMono = realtorRepository.findByUsername(realtorUsername)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Realtor not found")));

        return Mono.zip(clientMono, realtorMono)
                .map(tuple -> {
                    Chat chat = new Chat();
                    chat.setClient(tuple.getT1());
                    chat.setRealtor(tuple.getT2());
                    chat.setMessages(new ArrayList<>());
                    return chat;
                })
                .flatMap(chatRepository::save);
    }

    public Mono<Chat> sendMessage(String authHeader, String chatId, String message) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        String username = service.extractUsername(token);

        Mono<Sender> senderMono = Mono.zip(
                realtorRepository.findByUsername(username).defaultIfEmpty(null),
                clientRepository.findByUsername(username).defaultIfEmpty(null)
        ).map(tuple -> {
            if (tuple.getT1() != null) {
                return tuple.getT1();
            } else if (tuple.getT2() != null) {
                return tuple.getT2();
            }
            throw new UsernameNotFoundException("User not found as either Realtor or Client");
        });

        return senderMono.flatMap(sender ->
                chatRepository.findById(chatId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Chat not found with id: " + chatId)))
                        .map(chat -> {
                            if (chat.getMessages() == null) {
                                chat.setMessages(new ArrayList<>());
                            }
                            chat.getMessages().add(new Message(message, sender, new Date()));
                            return chat;
                        })
                        .flatMap(chatRepository::save)
                        .doOnSuccess(chat -> log.info("Message added to chat {} from {}",
                                chatId, sender.getClass().getSimpleName()))
        );
    }
}