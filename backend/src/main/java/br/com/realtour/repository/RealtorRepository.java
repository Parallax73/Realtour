package br.com.realtour.repository;

import br.com.realtour.entity.Realtor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface RealtorRepository extends MongoRepository<Realtor, String> {


    boolean existsByUsername(String username);

    boolean existsByCreci(String creci);

    Optional<Realtor> findByUsername(String username);
}
