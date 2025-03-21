package br.com.realtour.service;

import br.com.realtour.entity.Client;
import br.com.realtour.repository.ClientRepository;
import br.com.realtour.util.RegisterClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    ClientRepository repository;

    public ResponseEntity<?> createClient(RegisterClientDTO dto){
        try {
            Client client = new Client(dto);
            repository.save(client);
        }
    }

}
