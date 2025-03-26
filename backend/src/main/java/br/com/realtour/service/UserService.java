package br.com.realtour.service;

import br.com.realtour.entity.Client;
import br.com.realtour.entity.Realtor;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.repository.RealtorRepository;
import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.RegisterRealtorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    RealtorRepository realtorRepository;

    public Client createClient(RegisterClientDTO dto) {
        try {
            Client client = new Client(dto);
            return clientRepository.save(client);
        } catch (RuntimeException e) {
            throw new RuntimeException("Client already exists with this email");
        }
    }

    public Realtor createRealtor(RegisterRealtorDTO dto){

        //Fazer validação de estado e número (criar interface com as federações e fazer a validação).


            if (dto.creci().length()<9){
                throw new IllegalArgumentException("Creci is invalid, check the number or state.");
            }
            if (realtorRepository.existsByUsername(dto.username())){
                throw new IllegalArgumentException("Username already in use.");
            }
            Realtor client = new Realtor(dto);
            return realtorRepository.save(client);
        }
}
