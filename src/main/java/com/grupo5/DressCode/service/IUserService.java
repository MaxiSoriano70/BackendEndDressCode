package com.grupo5.DressCode.service;

import com.grupo5.DressCode.dto.UserDTO;
import com.grupo5.DressCode.security.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User createUser (User user);
    Optional<UserDTO> searchForId(int id);
    List<UserDTO> searchAll();
    Optional<User> updateUser(int id, User user);
    void deleteUser(Integer id);
}
