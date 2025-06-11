package br.com.realtour.controller;

import br.com.realtour.exception.InvalidCredientialsException;
import br.com.realtour.service.UserService;
import br.com.realtour.util.LoginDTO;
import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.RegisterRealtorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("http://localhost:4200")
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
                .map(token -> {
                    ResponseCookie cookie = ResponseCookie.from("__Secure-JWT", token)
                            .httpOnly(true)
                            .secure(true)
                            .path("/")
                            .maxAge(7 * 24 * 60 * 60)
                            .sameSite("Strict")
                            .build();

                    return ResponseEntity
                            .ok()
                            .header(HttpHeaders.SET_COOKIE, cookie.toString())
                            .body("Login successful");
                })
                .onErrorResume(InvalidCredientialsException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()))
                )
                .onErrorResume(Exception.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("An unexpected error occurred."))
                );
    }
}