package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.repository.IClotheRepository;
import com.grupo5.DressCode.service.IClotheService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClotheService implements IClotheService {
    private final IClotheRepository clotheRepository;

    public ClotheService(IClotheRepository clotheRepository) {
        this.clotheRepository = clotheRepository;
    }

    @Override
    public Clothe createClothe(Clothe clothe) {
        return clotheRepository.save(clothe);
    }

    @Override
    public Optional<Clothe> searchForId(int id) {
        return clotheRepository.findById(id);
    }

    @Override
    public List<Clothe> searchAll() {
        return clotheRepository.findAll();
    }

    @Override
    public void updateClothe(Clothe clothe) {
        clotheRepository.save(clothe);
    }

    @Override
    public void deleteClothe(Integer id) {
        clotheRepository.deleteById(id);
    }
}