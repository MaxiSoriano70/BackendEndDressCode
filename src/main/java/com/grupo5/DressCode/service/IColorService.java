package com.grupo5.DressCode.service;

import com.grupo5.DressCode.dto.ColorDTO;
import com.grupo5.DressCode.entity.Color;

import java.util.List;
import java.util.Optional;

public interface IColorService {
    ColorDTO createColor(ColorDTO colorDTO);
    Optional<ColorDTO> searchForId(int id);
    List<ColorDTO> searchAll();
    void updateColor(int id, ColorDTO color);
    void deleteColor(Integer id);
}
