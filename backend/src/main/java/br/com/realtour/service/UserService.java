package br.com.realtour.service;

import br.com.realtour.entity.Client;
import br.com.realtour.entity.Realtor;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.repository.RealtorRepository;
import br.com.realtour.util.LoginDTO;
import br.com.realtour.util.States;
import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.RegisterRealtorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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




    public Client createClient(RegisterClientDTO dto) {

        if (clientRepository.existsByUsername(dto.username())){
            throw new IllegalArgumentException("Username already in use");
        }
        Client client = new Client(dto);
        client.setPassword(encoder.encode(dto.password()));
        return clientRepository.save(client);

    }

    public Realtor createRealtor(RegisterRealtorDTO dto){

            if (dto.creci().length()<9 || !validateCreci(dto.creci())){
                throw new IllegalArgumentException("Creci is invalid, check the number or state.");
            }
            if (realtorRepository.existsByUsername(dto.username())){
                throw new IllegalArgumentException("Username already in use.");
            }

            Realtor realtor = new Realtor(dto);
            realtor.setPassword(encoder.encode(dto.password()));
            return realtorRepository.save(realtor);
        }


    public String loginRealtor(LoginDTO dto){

        try{
           if (realtorRepository.findByUsername(dto.username()).isPresent()){
               Realtor realtor = realtorRepository.findByUsername(dto.username()).get();
               if (encoder.matches(dto.password(), realtor.getPassword())){
                   return jwtService.generateToken(realtor.getUsername());
               }
           }
        } catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("Username not found");
        }
        return null;
    }

    public String loginClient(LoginDTO dto){

        try{
            if (clientRepository.findByUsername(dto.username()).isPresent()){
                Client client = clientRepository.findByUsername(dto.username()).get();
                if (encoder.matches(dto.password(), client.getPassword())){
                    return jwtService.generateToken(client.getUsername());
                }
            }
        } catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("Username not found");
        }
        return null;
    }



    private boolean validateCreci(String creci){
        return States.isValidState(creci.substring(0,2).toUpperCase()) && !realtorRepository.existsByCreci(creci);
        }
}
