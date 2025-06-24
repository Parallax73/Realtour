package br.com.realtour.service;

import br.com.realtour.entity.Client;
import br.com.realtour.entity.Realtor;
import br.com.realtour.entity.Unit;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.repository.RealtorRepository;
import br.com.realtour.repository.UnitRepository;
import br.com.realtour.util.CreateUnitDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UnitService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RealtorRepository realtorRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UnitRepository unitRepository;

    public Mono<Void> saveUnitToClient(String token, String unitId) {
        String email = jwtService.extractEmail(token);

        return Mono.zip(
                clientRepository.findByEmail(email)
                        .switchIfEmpty(Mono.error(new RuntimeException("Client not found"))),
                unitRepository.findById(unitId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Unit not found")))
        ).flatMap(tuple -> {
            Client client = tuple.getT1();
            Unit unit = tuple.getT2();

            client.addUnits(unit);
            return clientRepository.save(client);
        }).then();
    }

    public Mono<Void> saveUnitToRealtor(String token, String unitId) {
        String username = jwtService.extractEmail(token);

        return Mono.zip(
                realtorRepository.findByEmail(username)
                        .switchIfEmpty(Mono.error(new RuntimeException("Realtor not found"))),
                unitRepository.findById(unitId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Unit not found")))
        ).flatMap(tuple -> {
            Realtor realtor = tuple.getT1();
            Unit unit = tuple.getT2();

            realtor.addUnits(unit);
            return realtorRepository.save(realtor);
        }).then();
    }

    public Mono<Unit> createUnit(String token, CreateUnitDTO dto) {
        String email = jwtService.extractEmail(token);
        log.info(email);

        return realtorRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("Realtor not found")))
                .map(realtor -> {
                    Unit unit = new Unit(dto);
                    unit.setRealtor(realtor);
                    return unit;
                })
                .flatMap(unitRepository::save);
    }
}