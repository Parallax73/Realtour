package br.com.realtour.repository;

import br.com.realtour.entity.Realtor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RealtorRepository extends ReactiveMongoRepository<Realtor, String> {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByCreci(String creci);
    Mono<Realtor> findByUsername(String username);
    Mono<Realtor> findByEmail(String email);
}