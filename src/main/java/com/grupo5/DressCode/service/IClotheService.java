package com.grupo5.DressCode.service;

import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.security.dto.ClothesDTO;

import java.util.List;
import java.util.Optional;

public interface IClotheService {
    Clothe createClothe(ClothesDTO clothesDTO);
    Optional<Clothe> searchForId(int id);
    List<Clothe> searchAll();
    Clothe updateClothe(int id, ClothesDTO clothesDTO);
    void deleteClothe(Integer id);
}
