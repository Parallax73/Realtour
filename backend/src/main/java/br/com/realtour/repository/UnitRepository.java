package br.com.realtour.repository;

import br.com.realtour.entity.Realtor;
import br.com.realtour.entity.Unit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface UnitRepository extends ReactiveMongoRepository<Unit,String> {
    Flux<Unit> findByRealtor(Realtor realtor);
}