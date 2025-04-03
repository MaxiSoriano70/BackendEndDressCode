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

                    auth.requestMatchers("/auth/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/clothe/**", "/clothe/search", "/category/**", "/color/**", "/attribute/**").permitAll();

                    // Endpoints accesibles para ADMIN y USER
                    auth.requestMatchers(HttpMethod.PUT, "/user/**").hasAnyRole("ADMIN", "USER");

                    auth.requestMatchers(HttpMethod.GET, "/user/{id}").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers(HttpMethod.POST, "/user/{userId}/favorite/{clotheId}").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers(HttpMethod.DELETE, "/user/{userId}/favorite/{clotheId}").hasAnyRole("ADMIN", "USER");

                    auth.requestMatchers(HttpMethod.POST, "/reservations/**").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers(HttpMethod.PUT, "/reservations/**").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers(HttpMethod.PUT, "/reservations/{id}/confirm-payment").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers(HttpMethod.GET, "/reservations/user/{userId}").hasAnyRole("ADMIN", "USER");

                    auth.requestMatchers(HttpMethod.GET, "/reservations/{id}").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers(HttpMethod.DELETE, "/reservations/{reservationId}/item/{clotheId}").hasAnyRole("ADMIN", "USER");

                    auth.requestMatchers(HttpMethod.POST, "/review").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers(HttpMethod.GET, "/review/{id}").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers(HttpMethod.PUT, "/review/{id}").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers(HttpMethod.DELETE, "/review/{id}").hasAnyRole("ADMIN", "USER");

                    auth.requestMatchers(HttpMethod.GET, "/review/clothe/{clotheId}").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers(HttpMethod.GET, "/review/user/{userId}").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers(HttpMethod.GET, "/review/clothe/{clotheId}/rating").hasAnyRole("ADMIN", "USER");

                    // Endpoints accesibles solo para ADMIN
                    auth.requestMatchers(HttpMethod.GET, "/user/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/user/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.POST, "/clothe/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/clothe/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/clothe/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.POST, "/category/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/category/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/category/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.POST, "/attribute/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/attribute/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/attribute/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.POST, "/color/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/color/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/color/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.POST, "/imagen/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/imagen/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/imagen/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.DELETE, "/reservations/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/reservations/cancel-pending").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/reservations/{id}/return-clothe").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/review").hasRole("ADMIN");

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