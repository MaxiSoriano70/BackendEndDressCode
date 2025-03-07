package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.entity.Category;
import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.entity.Color;
import com.grupo5.DressCode.repository.ICategoryRepository;
import com.grupo5.DressCode.repository.IClotheRepository;
import com.grupo5.DressCode.repository.IColorRepository;
import com.grupo5.DressCode.service.IClotheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClotheService implements IClotheService {
    private final IClotheRepository clotheRepository;
    @Autowired
    private IColorRepository colorRepository;
    @Autowired
    private ICategoryRepository categoryRepository;

    public ClotheService(IClotheRepository clotheRepository) {
        this.clotheRepository = clotheRepository;
    }

    @Override
    public Clothe createClothe(Clothe clothe) {
        Optional<Category> categoryOpt = categoryRepository.findById(clothe.getCategory().getCategoryId());
        if (!categoryOpt.isPresent()){
            throw new RuntimeException("Categoría no encontrada");
        }
        Optional<Color> colorOptional = colorRepository.findById(clothe.getColor().getColorId());
        if (colorOptional.isEmpty()){
            throw new RuntimeException("Color no encontrado");
        }
        Clothe newClothe = new Clothe();
        newClothe.setSku(clothe.getSku());
        newClothe.setDescription(clothe.getDescription());
        newClothe.setSize(clothe.getSize());
        newClothe.setName(clothe.getName());
        newClothe.setPrice(clothe.getPrice());
        newClothe.setStock(clothe.getStock());
        newClothe.setActive(clothe.isActive());
        newClothe.setCategory(categoryOpt.get());
        newClothe.setColor(colorOptional.get());
        return clotheRepository.save(newClothe);
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