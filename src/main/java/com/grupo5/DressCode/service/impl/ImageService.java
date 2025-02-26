package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.entity.Image;
import com.grupo5.DressCode.repository.IImageRepository;
import com.grupo5.DressCode.service.IImageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ImageService implements IImageService {
    private IImageRepository imageRepository;

    public ImageService(IImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image createImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Optional<Image> searchForId(int id) {
        return imageRepository.findById(id);
    }

    @Override
    public List<Image> searchAll() {
        return imageRepository.findAll();
    }

    @Override
    public void updateImage(Image image) {
        imageRepository.save(image);
    }


    @Override
    public void deleteImage(Integer id) {
        imageRepository.deleteById(id);
    }
}
