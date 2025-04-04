package br.com.realtour.repository;

import br.com.realtour.entity.Realtor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RealtorRepository extends MongoRepository<Realtor, String> {


    boolean existsByUsername(String username);

    boolean existsByCreci(String creci);

    Realtor findByUsername(String username);
}
