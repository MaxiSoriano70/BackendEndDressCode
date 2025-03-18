package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.dto.ColorDTO;
import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.entity.Color;
import com.grupo5.DressCode.repository.IClotheRepository;
import com.grupo5.DressCode.repository.IColorRepository;
import com.grupo5.DressCode.service.IColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColorService implements IColorService {
    private final IColorRepository colorRepository;
    @Autowired
    private IClotheRepository clotheRepository;

    public ColorService(IColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public ColorDTO createColor(ColorDTO colorDTO) {
        Color color = new Color();
        color.setName(colorDTO.getName());
        Color savedColor = colorRepository.save(color);
        return new ColorDTO(savedColor.getColorId(), savedColor.getName());
    }

    @Override
    public Optional<ColorDTO> searchForId(int id) {
        Optional<Color> color = colorRepository.findById(id);
        return color.map(c -> new ColorDTO(c.getColorId(), c.getName()));
    }

    @Override
    public List<ColorDTO> searchAll() {
        return colorRepository.findAll().stream()
                .map(color -> new ColorDTO(color.getColorId(), color.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateColor(int id, ColorDTO colorDTO) {
        Optional<Color> existingColor = colorRepository.findById(id);
        if (existingColor.isEmpty()) {
            throw new RuntimeException("Color no encontrado");
        }

        Color color = existingColor.get();

        if (colorDTO.getName() != null && !colorDTO.getName().isEmpty()) {
            color.setName(colorDTO.getName());
        } else {
            throw new RuntimeException("El nombre del color no puede estar vacío.");
        }
        colorRepository.save(color);
    }

    @Override
    public void deleteColor(Integer id) {
        // Buscar el color a eliminar
        Optional<Color> colorOpt = colorRepository.findById(id);
        if (colorOpt.isEmpty()) {
            throw new RuntimeException("Color no encontrado");
        }
        Color color = colorOpt.get();
        // Buscar todas las prendas asociadas con este color
        List<Clothe> clothesWithColor = clotheRepository.findByColor(color);
        // Marcar todas las prendas asociadas como eliminadas lógicamente
        for (Clothe clothe : clothesWithColor) {
            clothe.deleteLogically();
            clothe.setColor(null);
        }
        // Guardar los cambios en las prendas
        clotheRepository.saveAll(clothesWithColor);
        // Eliminar el color
        colorRepository.delete(color);
    }
}
