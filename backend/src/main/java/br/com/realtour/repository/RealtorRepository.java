package br.com.realtour.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RealtorRepository extends MongoRepository<Realtor, String> {
}
