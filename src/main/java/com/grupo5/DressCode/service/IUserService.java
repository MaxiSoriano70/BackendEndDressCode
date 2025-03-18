package com.grupo5.DressCode.service;

import com.grupo5.DressCode.dto.UserDTO;
import com.grupo5.DressCode.security.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserDTO createUser (UserDTO userDTO);
    Optional<UserDTO> searchForId(int id);
    List<UserDTO> searchAll();
    Optional<UserDTO> updateUser(int id, UserDTO userDTO);
    void deleteUser(Integer id);
    Optional<UserDTO> addFavorite(int userId, int clotheId);
    Optional<UserDTO> removeFavorite(int userId, int clotheId);
}
