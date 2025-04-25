package br.com.realtour.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthFilter filter;

    final AuthenticationProvider provider;

    public SecurityConfig(JwtAuthFilter filter,AuthenticationProvider provider) {
        this.provider = provider;
        this.filter = filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
         http.authorizeHttpRequests(authorize ->
                 authorize.requestMatchers(
                         "/api/v1/users/register-realtor",
                                 "/api/v1/users/login-realtor")
                         .permitAll()
                         .anyRequest()
                         .authenticated())
                 .authenticationProvider(provider)
                 .csrf(AbstractHttpConfigurer::disable)
                 .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

