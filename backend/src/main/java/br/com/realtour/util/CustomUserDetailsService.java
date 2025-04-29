
package br.com.realtour.service;

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

  public CustomUserDetailsService(ClientRepository rep1,RealtorRepository rep2){
      this.clientRepository=rep1;
      this.realtorRepository=rep2;
  }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      if(clientRepository.findByUsername(username).isEmpty()) {
          try {
              Realtor realtor = realtorRepository.findByUsername(username).get();

              Set<GrantedAuthority> authorities = realtor
                      .getRoles()
                      .stream()
                      .map((role -> new SimpleGrantedAuthority(role.getRole().name())))
                      .collect(Collectors.toSet());

              return new User(realtor.getUsername(),
                      realtor.getPassword(),
                      authorities);
          } catch (UsernameNotFoundException e){
              throw  new UsernameNotFoundException("Username not found");
          }
      }
        Client client = clientRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        Set<GrantedAuthority> authorities = client
                .getRoles()
                .stream()
                .map((role -> new SimpleGrantedAuthority(role.getRole().name())))
                .collect(Collectors.toSet());

        return new User(client.getUsername(),
                client.getPassword(),
                authorities);
    }



}



