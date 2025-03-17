package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.dto.ClotheDTO;
import com.grupo5.DressCode.dto.ImagenDTO;
import com.grupo5.DressCode.entity.*;
import com.grupo5.DressCode.repository.IAttributeRepository;
import com.grupo5.DressCode.repository.ICategoryRepository;
import com.grupo5.DressCode.repository.IClotheRepository;
import com.grupo5.DressCode.repository.IColorRepository;
import com.grupo5.DressCode.service.IClotheService;
import com.grupo5.DressCode.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClotheService implements IClotheService {
    private final IClotheRepository clotheRepository;
    private final IColorRepository colorRepository;
    private final ICategoryRepository categoryRepository;
    private final IAttributeRepository attributeRepository;
    private final IImageService imageService;

    @Autowired
    public ClotheService(IClotheRepository clotheRepository, IColorRepository colorRepository,
                         ICategoryRepository categoryRepository, IAttributeRepository attributeRepository,
                         IImageService imageService) {
        this.clotheRepository = clotheRepository;
        this.colorRepository = colorRepository;
        this.categoryRepository = categoryRepository;
        this.attributeRepository = attributeRepository;
        this.imageService = imageService;
    }

    @Override
    public ClotheDTO createClothe(ClotheDTO clotheDTO) {
        // Validar existencia de la categoría
        Category category = categoryRepository.findById(clotheDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Validar existencia del color
        Color color = colorRepository.findById(clotheDTO.getColorId())
                .orElseThrow(() -> new RuntimeException("Color no encontrado"));

        // Crear una nueva prenda
        Clothe newClothe = new Clothe();
        newClothe.setSku(clotheDTO.getSku());
        newClothe.setDescription(clotheDTO.getDescription());
        newClothe.setSize(clotheDTO.getSize());
        newClothe.setName(clotheDTO.getName());
        newClothe.setPrice(clotheDTO.getPrice());
        newClothe.setStock(clotheDTO.getStock());
        newClothe.setActive(clotheDTO.isActive());
        newClothe.setCategory(category);
        newClothe.setColor(color);

        // Agregar imágenes
        if (clotheDTO.getImageUrls() != null) {
            clotheDTO.getImageUrls().forEach(imageUrl -> {
                Image image = new Image();
                image.setImageUrl(imageUrl);
                newClothe.getImages().add(image);
            });
        }

        // Agregar atributos
        if (clotheDTO.getAttributeIds() != null) {
            clotheDTO.getAttributeIds().forEach(attributeId -> {
                Optional<Attribute> attributeOpt = attributeRepository.findById(attributeId);
                attributeOpt.ifPresent(newClothe.getAttributes()::add);
            });
        }

        // Guardar la prenda
        Clothe savedClothe = clotheRepository.save(newClothe);

        return convertToDTO(savedClothe);
    }

    @Override
    public Optional<ClotheDTO> searchForId(int id) {
        return clotheRepository.findByClotheIdAndIsDeletedFalse(id)
                .map(this::convertToDTO);
    }

    @Override
    public List<ClotheDTO> searchAll() {
        return clotheRepository.findAll().stream()
                .filter(clothe -> !clothe.isDeleted()) // Filtrar prendas eliminadas lógicamente
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClotheDTO updateClothe(int id, ClotheDTO clotheDTO) {
        // Buscar la prenda por ID, asegurándonos de que no esté eliminada lógicamente
        Clothe existingClothe = clotheRepository.findByClotheIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada o eliminada lógicamente"));

        // Actualizar la prenda
        if (clotheDTO.getSku() != null && !clotheDTO.getSku().isEmpty()) {
            existingClothe.setSku(clotheDTO.getSku());
        }
        if (clotheDTO.getDescription() != null && !clotheDTO.getDescription().isEmpty()) {
            existingClothe.setDescription(clotheDTO.getDescription());
        }
        if (clotheDTO.getSize() != null) {
            existingClothe.setSize(clotheDTO.getSize());
        }
        if (clotheDTO.getName() != null && !clotheDTO.getName().isEmpty()) {
            existingClothe.setName(clotheDTO.getName());
        }
        if (clotheDTO.getPrice() != null && clotheDTO.getPrice() > 0) {
            existingClothe.setPrice(clotheDTO.getPrice());
        }
        if (clotheDTO.getStock() != null) {
            existingClothe.setStock(clotheDTO.getStock());
        }
        existingClothe.setActive(clotheDTO.isActive());

        // Actualizar categoría y color
        categoryRepository.findById(clotheDTO.getCategoryId()).ifPresent(existingClothe::setCategory);
        colorRepository.findById(clotheDTO.getColorId()).ifPresent(existingClothe::setColor);

        // Actualizar imágenes
        if (clotheDTO.getImageUrls() != null) {
            existingClothe.getImages().clear(); // Limpiar imágenes anteriores
            clotheDTO.getImageUrls().forEach(imageUrl -> {
                Image image = new Image();
                image.setImageUrl(imageUrl);
                existingClothe.getImages().add(image);
            });
        }

        // Actualizar atributos
        if (clotheDTO.getAttributeIds() != null) {
            Set<Attribute> updatedAttributes = new HashSet<>();
            clotheDTO.getAttributeIds().forEach(attributeId -> {
                attributeRepository.findById(attributeId).ifPresent(updatedAttributes::add);
            });
            existingClothe.setAttributes(updatedAttributes);
        }

        // Guardar la prenda actualizada
        Clothe updatedClothe = clotheRepository.save(existingClothe);

        return convertToDTO(updatedClothe);
    }

    @Override
    public void deleteClothe(Integer id) {
        Clothe clothe = clotheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada"));
        clothe.deleteLogically(); // Eliminación lógica
        clotheRepository.save(clothe);
    }

    private ClotheDTO convertToDTO(Clothe clothe) {
        return new ClotheDTO(
                clothe.getClotheId(),
                clothe.getSku(),
                clothe.getDescription(),
                clothe.getSize(),
                clothe.getName(),
                clothe.getPrice(),
                clothe.getStock(),
                clothe.isActive(),
                clothe.getCategory().getCategoryId(),
                clothe.getColor().getColorId(),
                clothe.getImages().stream().map(Image::getImageUrl).collect(Collectors.toSet()),
                clothe.getAttributes().stream().map(Attribute::getAttributeId).collect(Collectors.toSet()),
                clothe.isDeleted()
        );
    }
}
