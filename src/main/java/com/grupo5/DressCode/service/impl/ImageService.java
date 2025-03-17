package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.dto.ImagenDTO;
import com.grupo5.DressCode.entity.Image;
import com.grupo5.DressCode.repository.IImageRepository;
import com.grupo5.DressCode.service.IImageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService implements IImageService {
    private final IImageRepository imageRepository;

    public ImageService(IImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public ImagenDTO createImage(ImagenDTO imagenDTO) {
        Image image = new Image();
        image.setImageUrl(imagenDTO.getImageUrl());

        Image savedImage = imageRepository.save(image);

        return new ImagenDTO(savedImage.getImagenId(), savedImage.getImageUrl());
    }

    @Override
    public Optional<ImagenDTO> searchForId(int id) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            Image savedImage = image.get();
            return Optional.of(new ImagenDTO(savedImage.getImagenId(), savedImage.getImageUrl()));
        }
        return Optional.empty();
    }

    @Override
    public List<ImagenDTO> searchAll() {
        List<Image> images = imageRepository.findAll();
        return images.stream()
                .map(image -> new ImagenDTO(image.getImagenId(), image.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateImage(int id, ImagenDTO imagenDTO) {
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isEmpty()) {
            throw new RuntimeException("Imagen no encontrada");
        }

        Image existingImage = imageOptional.get();

        if (imagenDTO.getImageUrl() != null && !imagenDTO.getImageUrl().isEmpty()) {
            existingImage.setImageUrl(imagenDTO.getImageUrl());
        }

        imageRepository.save(existingImage);
    }


    @Override
    public void deleteImage(Integer id) {
        imageRepository.deleteById(id);
    }
}
