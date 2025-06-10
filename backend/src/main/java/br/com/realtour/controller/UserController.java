package br.com.realtour.controller;

import br.com.realtour.service.UserService;
import br.com.realtour.util.LoginDTO;
import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.RegisterRealtorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    @Autowired
    UserService service;

    @PostMapping("/register-client")
    public Mono<ResponseEntity<String>> registerClient(@RequestBody RegisterClientDTO dto) {
        return service.createClient(dto)
                .map(client -> ResponseEntity.status(HttpStatus.CREATED).body("Created with success"))
                .onErrorResume(IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage())))
                .onErrorResume(Exception.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("An unexpected error occurred.")));
    }

    @PostMapping("/register-realtor")
    public Mono<ResponseEntity<String>> registerRealtor(@RequestBody RegisterRealtorDTO dto) {
        return service.createRealtor(dto)
                .map(realtor -> ResponseEntity.status(HttpStatus.CREATED).body("Created with success"))
                .onErrorResume(IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage())))
                .onErrorResume(Exception.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("An unexpected error occurred." + e.getMessage())));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody LoginDTO dto) {
        return service.login(dto)
                .map(token -> ResponseEntity.ok().body(token))
                .onErrorResume(IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage())))
                .onErrorResume(Exception.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("An unexpected error occurred.")));
    }
}