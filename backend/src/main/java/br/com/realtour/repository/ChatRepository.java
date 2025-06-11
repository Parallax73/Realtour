package br.com.realtour.repository;

import br.com.realtour.entity.Chat;
import br.com.realtour.entity.Client;
import br.com.realtour.entity.Realtor;
import br.com.realtour.entity.Unit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {


    Mono<Chat> findByClientAndRealtorAndUnit(Client client, Realtor realtor, Unit unit);
}