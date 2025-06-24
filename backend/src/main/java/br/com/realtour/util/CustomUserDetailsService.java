package br.com.realtour.util;

import br.com.realtour.entity.Client;
import br.com.realtour.entity.Realtor;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.repository.RealtorRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService {

    private final ClientRepository clientRepository;
    private final RealtorRepository realtorRepository;

    public CustomUserDetailsService(ClientRepository clientRepository, RealtorRepository realtorRepository) {
        this.clientRepository = clientRepository;
        this.realtorRepository = realtorRepository;
    }

    public Mono<UserDetails> findByEmail(String email) {
        return realtorRepository.findByEmail(email)
                .map(this::convertRealtorToUserDetails)
                .switchIfEmpty(
                        clientRepository.findByEmail(email)
                                .map(this::convertClientToUserDetails)
                );
    }

    private UserDetails convertRealtorToUserDetails(Realtor realtor) {
        Set<GrantedAuthority> authorities = realtor.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toSet());

        return new User(
                realtor.getEmail(),
                realtor.getPassword(),
                authorities
        );
    }

    private UserDetails convertClientToUserDetails(Client client) {
        Set<GrantedAuthority> authorities = client.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toSet());

        return new User(
                client.getEmail(),
                client.getPassword(),
                authorities
        );
    }
}