package br.com.realtour.controller;



import br.com.realtour.service.UserService;
import br.com.realtour.util.LoginDTO;
import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.RegisterRealtorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    @Autowired
    UserService service;


    @PostMapping("/create-client")
    public ResponseEntity<?> registerClient(@RequestBody RegisterClientDTO dto) {
        try {
            this.service.createClient(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created with success");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/register-realtor")
    public ResponseEntity<?> registerRealtor(@RequestBody RegisterRealtorDTO dto) {
        try {
            this.service.createRealtor(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created with success");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred." + e.getMessage());
        }
    }

    @GetMapping("/login-realtor")
    public ResponseEntity<?> loginRealtor(@RequestBody LoginDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.service.loginRealtor(dto));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

