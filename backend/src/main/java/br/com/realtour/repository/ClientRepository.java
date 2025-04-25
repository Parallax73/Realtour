package br.com.realtour.repository;



import br.com.realtour.entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface ClientRepository  extends MongoRepository<Client, String> {

    boolean existsByUsername(String username);

    Optional<Client> findByUsername(String username);
}
