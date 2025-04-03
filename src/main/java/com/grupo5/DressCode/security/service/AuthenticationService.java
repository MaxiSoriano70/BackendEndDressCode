package com.grupo5.DressCode.security.service;

import com.grupo5.DressCode.security.dto.AuthenticationRequest;
import com.grupo5.DressCode.security.dto.AuthenticationResponse;
import com.grupo5.DressCode.security.dto.RegisterRequest;
import com.grupo5.DressCode.security.entity.User;
import com.grupo5.DressCode.security.repository.IUserRepository;
import com.grupo5.DressCode.utils.ERol;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private JavaMailSender javaMailSender;
    public AuthenticationResponse register(RegisterRequest request) {
        ERol role = request.getRole() != null ? request.getRole() : ERol.USER;

        // Crear el usuario
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        // Guardar el usuario
        userRepository.save(user);

        // Crear el mensaje de bienvenida
        String asunto = "Bienvenido a DressCode";
        String cuerpo = "Hola " + user.getFirstName() + " " + user.getLastName() + ",\n\n" +
                "Bienvenido a DressCode. Nos alegra que te hayas unido a nuestra plataforma.\n" +
                "¡Disfruta de la experiencia!\n\n" +
                "http://localhost:5174/login" +
                "Correo: " + user.getEmail() +
                "Saludos,\nEl equipo de DressCode";

        // Enviar el correo
        enviarCorreo(user.getEmail(), asunto, cuerpo);

        // Generar el JWT
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .jwt(jwtToken)
                .build();
    }



    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  request.getEmail(),
                  request.getPassword()
          )
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .jwt(jwtToken)
                .build();
    }

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        try {
            MimeMessage mensaje = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setFrom("maximiliano.70.32.10.soriano@gmail.com");
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpo);

            javaMailSender.send(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
