package br.com.realtour.service;

import br.com.realtour.entity.Client;
import br.com.realtour.entity.Realtor;
import br.com.realtour.exception.InvalidCredientialsException;
import br.com.realtour.exception.UsernameAlreadyExistsException;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.repository.RealtorRepository;
import br.com.realtour.util.LoginDTO;
import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.RegisterRealtorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    RealtorRepository realtorRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    private PasswordEncoder encoder;

    public Mono<Client> createClient(RegisterClientDTO dto) {
        return clientRepository.existsByUsername(dto.username())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Username already exists"));
                    }
                    Client client = new Client(dto);
                    client.setPassword(encoder.encode(dto.password()));
                    return clientRepository.save(client);
                });
    }

    public Mono<Realtor> createRealtor(RegisterRealtorDTO dto) {
        return Mono.just(dto)
                .filterWhen(d -> validateCreci(d.creci()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid CRECI")))
                .flatMap(d -> realtorRepository.existsByUsername(d.username())
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(new UsernameAlreadyExistsException("Username already exists"));
                            }
                            return realtorRepository.existsByCreci(d.creci());
                        })
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(new IllegalArgumentException("CRECI already exists"));
                            }
                            Realtor realtor = new Realtor(dto);
                            realtor.setPassword(encoder.encode(dto.password()));
                            return realtorRepository.save(realtor);
                        }));
    }

    public Mono<String> login(LoginDTO dto) {


        return clientRepository.findByEmail(dto.email())
                .flatMap(client -> {
                    if (encoder.matches(dto.password(), client.getPassword())) {
                        return Mono.just(jwtService.generateToken(dto.email()));
                    }

                    return Mono.empty();
                })
                .switchIfEmpty(

                        realtorRepository.findByEmail(dto.email())
                                .flatMap(realtor -> {
                                    if (encoder.matches(dto.password(), realtor.getPassword())) {
                                        return Mono.just(jwtService.generateToken(dto.email()));
                                    }
                                    return Mono.empty();
                                })
                )
                .switchIfEmpty(Mono.error(new InvalidCredientialsException("Invalid credentials")));
    }



    private Mono<Boolean> validateCreci(String creci) {
        return Mono.just(creci)
                .map(c -> c.matches("[A-Z]{2}-\\d{6}"))  // Changed to 6 digits
                .filter(valid -> valid)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid CRECI format")));
    }
}