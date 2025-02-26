package com.grupo5.DressCode.service;

import com.grupo5.DressCode.entity.Color;

import java.util.List;
import java.util.Optional;

public interface IColorService {
    Color createColor(Color color);
    Optional<Color> searchForId(int id);
    List<Color> searchAll();
    void updateColor(Color color);
    void deleteColor(Integer id);
}
