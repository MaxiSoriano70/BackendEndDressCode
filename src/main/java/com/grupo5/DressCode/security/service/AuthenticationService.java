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

        // Crear el mensaje de bienvenida con el texto estilizado
        String asunto = "Bienvenido a DressCode";
        String cuerpo = "<p style='font-size: 36px; font-weight: bold; color: #D4AF37; text-align: center; text-shadow: 3px 3px 8px rgba(0, 0, 0, 0.5);'>Bienvenido a DressCode.</p>" +
                "<p style='font-size: 16px;'>Nos alegra que te hayas unido a nuestra plataforma.</p>" +
                "<p style='font-size: 18px; font-weight: bold; text-align: center; color: #D4AF37'>¡Disfruta de la experiencia!</p>" +
                "<p style='font-size: 16px;'>Inicia sesión aquí: <a href='http://localhost:5174/login' style='color: #FF6600; text-decoration: none; font-weight: bold;'>Inicia sesión</a></p>" +
                "<p style='font-size: 16px; color: #333333;'>Correo: <span style='color: #FF6600;'>" + user.getEmail() + "</span></p>" +
                "<p style='font-size: 16px;'>Saludos,<br><strong>El equipo de DressCode</strong></p>";


        // Enviar el correo
        enviarCorreo(user.getEmail(), asunto, cuerpo, user);

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

    public void enviarCorreo(String destinatario, String asunto, String cuerpo, User user) {
        try {
            MimeMessage mensaje = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setFrom("dresscodedh5c4@gmail.com");
            helper.setTo(destinatario);
            helper.setSubject(asunto);

            // Aquí agregamos el cuerpo del mensaje con HTML y estilos
            String contenidoHTML = "<html>" +
                    "<head>" +
                    "<style>" +
                    "body {" +
                    "    font-family: Arial, sans-serif;" +
                    "    color: #333333;" +
                    "    background-color: #f4f4f4;" +
                    "    padding: 20px;" +
                    "    text-align: center;" +
                    "}" +
                    "h1 {" +
                    "    font-size: 36px;" +
                    "    color: #000000;" +
                    "    margin-bottom: 20px;" +
                    "    text-aling: center" +
                    "}" +
                    "p {" +
                    "    font-size: 16px;" +
                    "    color: #333333;" +
                    "    line-height: 1.6;" +
                    "}" +
                    "a {" +
                    "    color: #FF6600;" +
                    "    text-decoration: none;" +
                    "    font-weight: bold;" +
                    "}" +
                    "a:hover {" +
                    "    text-decoration: underline;" +
                    "}" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<h1>Hola " + user.getFirstName() + " " + user.getLastName() + "!</h1>" +
                    cuerpo +
                    "</body>" +
                    "</html>";

            helper.setText(contenidoHTML, true);

            javaMailSender.send(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


