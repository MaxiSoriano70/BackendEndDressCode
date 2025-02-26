package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.entity.Color;
import com.grupo5.DressCode.repository.ICategoryRepository;
import com.grupo5.DressCode.repository.IColorRepository;
import com.grupo5.DressCode.service.IColorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ColorService implements IColorService {
    private IColorRepository colorRepository;

    public ColorService(IColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public Color createColor(Color color) {
        return colorRepository.save(color);
    }

    @Override
    public Optional<Color> searchForId(int id) {
        return colorRepository.findById(id);
    }

    @Override
    public List<Color> searchAll() {
        return colorRepository.findAll();
    }

    @Override
    public void updateColor(Color color) {
        colorRepository.save(color);
    }

    @Override
    public void deleteColor(Integer id) {
        colorRepository.deleteById(id);
    }
}
