package br.com.realtour.config;

import br.com.realtour.util.CustomUserDetailsService;
import br.com.realtour.service.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthManager(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String email = jwtService.extractEmail(token);
        return userDetailsService.findByEmail(email)
                .filter(userDetails -> jwtService.validateToken(token))
                .map(userDetails -> (Authentication) new UsernamePasswordAuthenticationToken(
                        userDetails, token, userDetails.getAuthorities()
                ))
                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid JWT")));
    }
}