package br.com.realtour.repository;

import br.com.realtour.entity.Chat;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
}