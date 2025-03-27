package br.com.realtour.service;

import br.com.realtour.entity.Client;
import br.com.realtour.entity.Realtor;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.repository.RealtorRepository;
import br.com.realtour.util.Estados;
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

        if (clientRepository.existsByUsername(dto.username())){
            throw new IllegalArgumentException("Username already in use");
        }
        Client client = new Client(dto);
        return clientRepository.save(client);

    }

    public Realtor createRealtor(RegisterRealtorDTO dto){

            if (dto.creci().length()<9 || !validateCreci(dto.creci())){
                throw new IllegalArgumentException("Creci is invalid, check the number or state.");
            }
            if (realtorRepository.existsByUsername(dto.username())){
                throw new IllegalArgumentException("Username already in use.");
            }
            Realtor client = new Realtor(dto);
            return realtorRepository.save(client);
        }

        private boolean validateCreci(String creci){
           return Estados.isValidState(creci.substring(0,2).toUpperCase()) && !realtorRepository.existsByCreci(creci);
        }

}
