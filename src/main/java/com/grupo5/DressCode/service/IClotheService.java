package com.grupo5.DressCode.service;

import com.grupo5.DressCode.entity.Clothe;

import java.util.List;
import java.util.Optional;

public interface IClotheService {
    Clothe createClothe(Clothe clothe);
    Optional<Clothe> searchForId(int id);
    List<Clothe> searchAll();
    void updateClothe(Clothe clothe);
    void deleteClothe(Integer id);
}
