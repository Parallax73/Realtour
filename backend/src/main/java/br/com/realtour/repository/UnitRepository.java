package br.com.realtour.repository;

import br.com.realtour.entity.Realtor;
import br.com.realtour.entity.Unit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UnitRepository extends MongoRepository<Unit,String> {

    List<Unit> findByRealtor(Realtor realtor);

}
