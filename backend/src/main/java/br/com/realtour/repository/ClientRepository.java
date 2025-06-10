package br.com.realtour.repository;

import br.com.realtour.entity.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
    Mono<Boolean> existsByUsername(String username);
    Mono<Client> findByUsername(String username);
    Mono<Client> findByEmail(String email);
}