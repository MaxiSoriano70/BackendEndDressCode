package com.grupo5.DressCode.security.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SegurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth ->
                {
                    // Permitir solicitudes OPTIONS para CORS preflight
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                    // Endpoints sin autenticación
                    auth.requestMatchers("/auth/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/clothe/**", "/category/**", "/color/**").permitAll(); // Añadido /color/**

                    // Endpoints accesibles solo para ADMIN
                    auth.requestMatchers(HttpMethod.POST, "/category/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/category/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/category/**").hasRole("ADMIN");

                    // Endpoints accesibles para ADMIN y USER
                    auth.requestMatchers(HttpMethod.PUT, "/user/**").hasAnyRole("ADMIN", "USER");

                    // Endpoints accesibles solo para ADMIN
                    auth.requestMatchers(HttpMethod.GET, "/user/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/user/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN");

                    // Endpoints con roles específicos
                    auth.requestMatchers(HttpMethod.POST, "/clothe/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/clothe/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/clothe/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.POST, "/color/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/color/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/color/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.POST, "/imagen/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/imagen/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/imagen/**").hasRole("ADMIN");

                    // Cualquier otra solicitud debe estar autenticada
                    auth.anyRequest().authenticated();
                })
                .csrf(config -> config.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .build();
    }
}