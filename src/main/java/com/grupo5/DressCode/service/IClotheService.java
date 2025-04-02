package com.grupo5.DressCode.service;

import com.grupo5.DressCode.dto.ClotheDTO;
import com.grupo5.DressCode.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IClotheService {
    ClotheDTO createClothe(ClotheDTO clotheDTO);
    Optional<ClotheDTO> searchForId(int id);
    List<ClotheDTO> searchAll();
    List<ClotheDTO> searchAllDelete();
    ClotheDTO updateClothe(int id, ClotheDTO clotheDTO);
    void deleteClothe(Integer id);
    /*List<Object[]> searchName(String name);*/
    List<ClotheDTO> searchName(String name);

}
