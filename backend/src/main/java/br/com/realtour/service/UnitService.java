package br.com.realtour.service;

import br.com.realtour.entity.Client;
import br.com.realtour.entity.Realtor;
import br.com.realtour.entity.Unit;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.repository.RealtorRepository;
import br.com.realtour.repository.UnitRepository;
import br.com.realtour.util.CreateUnitDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

    public Mono<Void> saveUnitToClient(String authHeader, String unitId) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        String username = jwtService.extractUsername(token);

        return Mono.zip(
                clientRepository.findByUsername(username)
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

    public Mono<Void> saveUnitToRealtor(String authHeader, String unitId) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        String username = jwtService.extractUsername(token);

        return Mono.zip(
                realtorRepository.findByUsername(username)
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

    public Mono<Unit> createUnit(String authHeader, CreateUnitDTO dto) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        String username = jwtService.extractUsername(token);

        return realtorRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new RuntimeException("Realtor not found")))
                .map(realtor -> {
                    Unit unit = new Unit(dto);
                    unit.setRealtor(realtor);
                    return unit;
                })
                .flatMap(unitRepository::save);
    }
}