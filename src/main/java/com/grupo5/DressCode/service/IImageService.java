package com.grupo5.DressCode.service;

import com.grupo5.DressCode.dto.ImagenDTO;
import com.grupo5.DressCode.entity.Image;

import java.util.List;
import java.util.Optional;

public interface IImageService {
    ImagenDTO createImage(ImagenDTO imagenDTO);
    Optional<ImagenDTO> searchForId(int id);
    List<ImagenDTO> searchAll();
    void updateImage(int id, ImagenDTO imagenDTO);
    void deleteImage(Integer id);
}
