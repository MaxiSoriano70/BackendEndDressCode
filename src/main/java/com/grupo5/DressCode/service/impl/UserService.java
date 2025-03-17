package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.dto.UserDTO;
import com.grupo5.DressCode.security.entity.User;
import com.grupo5.DressCode.security.repository.IUserRepository;
import com.grupo5.DressCode.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRole(userDTO.getRole());

        User savedUser = userRepository.save(user);

        return new UserDTO(
                savedUser.getUsuarioId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    @Override
    public Optional<UserDTO> searchForId(int id) {
        return userRepository.findById(id)
                .map(user -> new UserDTO(
                        user.getUsuarioId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getRole()
                ));
    }

    @Override
    public List<UserDTO> searchAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(
                        user.getUsuarioId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getRole()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> updateUser(int id, UserDTO userDTO) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();

            if (userDTO.getFirstName() != null && !userDTO.getFirstName().isEmpty()) {
                existingUser.setFirstName(userDTO.getFirstName());
            }
            if (userDTO.getLastName() != null && !userDTO.getLastName().isEmpty()) {
                existingUser.setLastName(userDTO.getLastName());
            }
            if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
                existingUser.setEmail(userDTO.getEmail());
            }
            if (userDTO.getRole() != null) {
                existingUser.setRole(userDTO.getRole());
            }

            userRepository.save(existingUser);

            return Optional.of(new UserDTO(
                    existingUser.getUsuarioId(),
                    existingUser.getFirstName(),
                    existingUser.getLastName(),
                    existingUser.getEmail(),
                    existingUser.getRole()
            ));
        }
        return Optional.empty();
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
