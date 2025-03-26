package br.com.realtour.controller;


import br.com.realtour.entity.Realtor;
import br.com.realtour.service.UserService;
import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.RegisterRealtorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService service;



    @GetMapping("create-client")
    public ResponseEntity<HttpStatus> registerClient(@RequestBody RegisterClientDTO dto){
        this.service.createClient(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("register-realtor")
    public ResponseEntity<?> registerRealtor(@RequestBody RegisterRealtorDTO dto) {
        try {
            Realtor realtor = this.service.createRealtor(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(realtor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}

