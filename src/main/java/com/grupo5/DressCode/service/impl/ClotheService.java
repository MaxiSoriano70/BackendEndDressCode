package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.entity.*;
import com.grupo5.DressCode.repository.IAttributeRepository;
import com.grupo5.DressCode.repository.ICategoryRepository;
import com.grupo5.DressCode.repository.IClotheRepository;
import com.grupo5.DressCode.repository.IColorRepository;
import com.grupo5.DressCode.security.dto.ClothesDTO;
import com.grupo5.DressCode.service.IClotheService;
import com.grupo5.DressCode.service.IImageService;
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

    @Autowired
    private IAttributeRepository attributeRepository;

    @Autowired
    private IImageService imageService;

    public ClotheService(IClotheRepository clotheRepository) {
        this.clotheRepository = clotheRepository;
    }

    @Override
    public Clothe createClothe(ClothesDTO clothesDTO) {
        Optional<Category> categoryOpt = categoryRepository.findById(clothesDTO.getCategoryID());
        if (!categoryOpt.isPresent()){
            throw new RuntimeException("Categoría no encontrada");
        }
        Optional<Color> colorOptional = colorRepository.findById(clothesDTO.getColorID());
        if (colorOptional.isEmpty()){
            throw new RuntimeException("Color no encontrado");
        }
        Clothe newClothe = new Clothe();
        newClothe.setSku(clothesDTO.getSku());
        newClothe.setDescription(clothesDTO.getDescription());
        newClothe.setSize(clothesDTO.getSize());
        newClothe.setName(clothesDTO.getName());
        newClothe.setPrice(clothesDTO.getPrice().floatValue());
        newClothe.setStock(clothesDTO.getStock());
        newClothe.setActive(clothesDTO.isActive());
        newClothe.setCategory(categoryOpt.get());
        newClothe.setColor(colorOptional.get());
        // Asocia las imágenes según los IDs enviados.
        if (clothesDTO.getImageIds() != null) {
            for (Integer imageId : clothesDTO.getImageIds()){
                Optional<Image> imageOpt = imageService.searchForId(imageId);
                imageOpt.ifPresent(image -> newClothe.getImages().add(image));
            }
        }

        if (clothesDTO.getAttributeIds() != null) {
            for (Integer attributeId : clothesDTO.getAttributeIds()) {
                Optional<Attribute> attributeOpt = attributeRepository.findById(attributeId);
                attributeOpt.ifPresent(attribute -> newClothe.getAttributes().add(attribute));
            }
        }

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
    public Clothe updateClothe(int id, ClothesDTO clothesDTO) {
        Optional<Clothe> clotheOpt = clotheRepository.findById(id);
        if (!clotheOpt.isPresent()) {
            throw new RuntimeException("Prenda no encontrada");
        }

        Clothe existingClothe = clotheOpt.get();

        // Actualizar datos básicos
        existingClothe.setSku(clothesDTO.getSku());
        existingClothe.setDescription(clothesDTO.getDescription());
        existingClothe.setSize(clothesDTO.getSize());
        existingClothe.setName(clothesDTO.getName());
        existingClothe.setPrice(clothesDTO.getPrice().floatValue());
        existingClothe.setStock(clothesDTO.getStock());
        existingClothe.setActive(clothesDTO.isActive());

        // Validar y actualizar categoría
        Optional<Category> categoryOpt = categoryRepository.findById(clothesDTO.getCategoryID());
        if (!categoryOpt.isPresent()) {
            throw new RuntimeException("Categoría no encontrada");
        }
        existingClothe.setCategory(categoryOpt.get());

        // Validar y actualizar color
        Optional<Color> colorOpt = colorRepository.findById(clothesDTO.getColorID());
        if (!colorOpt.isPresent()) {
            throw new RuntimeException("Color no encontrado");
        }
        existingClothe.setColor(colorOpt.get());

        // Actualizar imágenes: eliminar las anteriores y agregar las nuevas
        existingClothe.getImages().clear();
        if (clothesDTO.getImageIds() != null) {
            for (Integer imageId : clothesDTO.getImageIds()) {
                Optional<Image> imageOpt = imageService.searchForId(imageId);
                imageOpt.ifPresent(image -> existingClothe.getImages().add(image));
            }
        }

        // Actualizar atributos: eliminar los anteriores y agregar los nuevos
        existingClothe.getAttributes().clear();
        if (clothesDTO.getAttributeIds() != null) {
            for (Integer attributeId : clothesDTO.getAttributeIds()) {
                Optional<Attribute> attributeOpt = attributeRepository.findById(attributeId);
                attributeOpt.ifPresent(attribute -> existingClothe.getAttributes().add(attribute));
            }
        }

        // Guardar cambios en la base de datos
        return clotheRepository.save(existingClothe);
    }


    @Override
    public void deleteClothe(Integer id) {
        clotheRepository.deleteById(id);
    }
}
