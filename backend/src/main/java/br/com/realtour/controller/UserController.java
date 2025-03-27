package br.com.realtour.controller;


import br.com.realtour.entity.Client;
import br.com.realtour.entity.Realtor;
import br.com.realtour.service.UserService;
import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.RegisterRealtorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService service;



    @PostMapping("create-client")
    public ResponseEntity<?> registerClient(@RequestBody RegisterClientDTO dto){
        try {
            Client client = this.service.createClient(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created with success");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("register-realtor")
    public ResponseEntity<?> registerRealtor(@RequestBody RegisterRealtorDTO dto) {
        try {
            Realtor realtor = this.service.createRealtor(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created with success");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}

