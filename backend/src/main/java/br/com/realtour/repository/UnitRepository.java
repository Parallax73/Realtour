package br.com.realtour.repository;

import br.com.realtour.entity.Realtor;
import br.com.realtour.entity.Unit;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UnitRepository extends ReactiveMongoRepository<Unit,String> {
    Flux<Unit> findByRealtor(Realtor realtor);

    Mono<Unit> findById(@NotNull String id);
}