package br.com.realtour.repository;



import br.com.realtour.entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository  extends MongoRepository<Client, String> {

    boolean existsByUsername(String username);
}
