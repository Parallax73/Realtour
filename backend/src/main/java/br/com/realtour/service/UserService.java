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
    ClientRepository ClientRepository;

    @Autowired
    RealtorRepository RealtorRepository;

    public Client createClient(RegisterClientDTO dto) {
        try {
            Client client = new Client(dto);
            return ClientRepository.save(client);
        } catch (RuntimeException e) {
            throw new RuntimeException("Client already exists with this email");
        }
    }

    public Realtor createRealtor(RegisterRealtorDTO dto){
        try {
            Realtor client = new Realtor(dto);
            return RealtorRepository.save(client);
        } catch (RuntimeException e) {
            throw new RuntimeException("Client already exists with this email");
        }
    }

}
