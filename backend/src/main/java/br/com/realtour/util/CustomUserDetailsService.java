package br.com.realtour.util;

import br.com.realtour.entity.Client;
import br.com.realtour.entity.Realtor;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.repository.RealtorRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final RealtorRepository realtorRepository;

    public CustomUserDetailsService(ClientRepository clientRepository, RealtorRepository realtorRepository) {
        this.clientRepository = clientRepository;
        this.realtorRepository = realtorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Realtor realtor = realtorRepository.findByUsername(username).block();
        if (realtor != null) {
            return convertRealtorToUserDetails(realtor);
        }


        Client client = clientRepository.findByUsername(username).block();
        if (client != null) {
            return convertClientToUserDetails(client);
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }

    private UserDetails convertRealtorToUserDetails(Realtor realtor) {
        Set<GrantedAuthority> authorities = realtor.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toSet());

        return new User(
                realtor.getUsername(),
                realtor.getPassword(),
                authorities
        );
    }

    private UserDetails convertClientToUserDetails(Client client) {
        Set<GrantedAuthority> authorities = client.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toSet());

        return new User(
                client.getUsername(),
                client.getPassword(),
                authorities
        );
    }
}