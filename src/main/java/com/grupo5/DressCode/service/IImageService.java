package com.grupo5.DressCode.service;

import com.grupo5.DressCode.entity.Image;

import java.util.List;
import java.util.Optional;

public interface IImageService {
    Image createImage(Image image);
    Optional<Image> searchForId(int id);
    List<Image> searchAll();
    void updateImage(Image image);
    void deleteImage(Integer id);
}
