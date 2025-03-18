package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.dto.UserDTO;
import com.grupo5.DressCode.dto.ClotheDTO;
import com.grupo5.DressCode.entity.Attribute;
import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.entity.Image;
import com.grupo5.DressCode.repository.IClotheRepository;
import com.grupo5.DressCode.security.entity.User;
import com.grupo5.DressCode.security.repository.IUserRepository;
import com.grupo5.DressCode.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IClotheRepository clotheRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRole(userDTO.getRole());

        if (userDTO.getFavoriteClothes() != null) {
            userDTO.getFavoriteClothes().forEach(clotheDTO -> {
                Optional<Clothe> clotheOpt = clotheRepository.findById(clotheDTO.getClotheId());
                clotheOpt.ifPresent(user.getFavoriteClothes()::add);
            });
        }

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    public Optional<UserDTO> searchForId(int id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public List<UserDTO> searchAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
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

            if (userDTO.getFavoriteClothes() != null) {
                existingUser.getFavoriteClothes().clear();
                userDTO.getFavoriteClothes().forEach(clotheDTO -> {
                    Optional<Clothe> clotheOpt = clotheRepository.findById(clotheDTO.getClotheId());
                    clotheOpt.ifPresent(existingUser.getFavoriteClothes()::add);
                });
            }

            userRepository.save(existingUser);
            return Optional.of(convertToDTO(existingUser));
        }
        return Optional.empty();
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    // Agregar favorito
    @Override
    public Optional<UserDTO> addFavorite(int userId, int clotheId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Clothe> clotheOpt = clotheRepository.findById(clotheId);

        if (userOpt.isPresent() && clotheOpt.isPresent()) {
            User user = userOpt.get();
            Clothe clothe = clotheOpt.get();

            if (!user.getFavoriteClothes().contains(clothe)) {
                user.getFavoriteClothes().add(clothe);
                userRepository.save(user);
                return Optional.of(convertToDTO(user));
            }
        }
        return Optional.empty();
    }

    // Eliminar favorito
    @Override
    public Optional<UserDTO> removeFavorite(int userId, int clotheId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Clothe> clotheOpt = clotheRepository.findById(clotheId);

        if (userOpt.isPresent() && clotheOpt.isPresent()) {
            User user = userOpt.get();
            Clothe clothe = clotheOpt.get();

            if (user.getFavoriteClothes().contains(clothe)) {
                user.getFavoriteClothes().remove(clothe);
                userRepository.save(user);
                return Optional.of(convertToDTO(user));
            }
        }
        return Optional.empty();
    }

    private UserDTO convertToDTO(User user) {
        Set<ClotheDTO> favoriteClothesDTO = user.getFavoriteClothes().stream()
                .map(clothe -> new ClotheDTO(
                        clothe.getClotheId(),
                        clothe.getSku(),
                        clothe.getDescription(),
                        clothe.getSize(),
                        clothe.getName(),
                        clothe.getPrice(),
                        clothe.getStock(),
                        clothe.isActive(),
                        clothe.getCategory() != null ? clothe.getCategory().getCategoryId() : null,
                        clothe.getColor() != null ? clothe.getColor().getColorId() : null,
                        clothe.getImages().stream().map(Image::getImageUrl).collect(Collectors.toSet()),
                        clothe.getAttributes().stream().map(Attribute::getAttributeId).collect(Collectors.toSet()),
                        clothe.isDeleted()
                ))
                .collect(Collectors.toSet());

        return new UserDTO(
                user.getUsuarioId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                favoriteClothesDTO
        );
    }
}
