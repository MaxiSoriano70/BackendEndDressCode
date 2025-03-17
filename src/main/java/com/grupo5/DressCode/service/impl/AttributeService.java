package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.dto.AttributeDTO;
import com.grupo5.DressCode.entity.Attribute;
import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.repository.IAttributeRepository;
import com.grupo5.DressCode.repository.IClotheRepository;
import com.grupo5.DressCode.service.IAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttributeService implements IAttributeService {
    private final IAttributeRepository attributeRepository;

    @Autowired
    private IClotheRepository clotheRepository;

    public AttributeService(IAttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    @Override
    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        Attribute attribute = new Attribute();
        attribute.setName(attributeDTO.getName());
        attribute.setIconUrl(attributeDTO.getIconUrl());

        Attribute savedAttribute = attributeRepository.save(attribute);

        return new AttributeDTO(savedAttribute.getAttributeId(), savedAttribute.getName(), savedAttribute.getIconUrl());
    }

    @Override
    public Optional<AttributeDTO> searchForId(int id) {
        Optional<Attribute> attribute = attributeRepository.findById(id);
        if (attribute.isPresent()) {
            Attribute savedAttribute = attribute.get();
            AttributeDTO attributeDTO = new AttributeDTO(savedAttribute.getAttributeId(), savedAttribute.getName(), savedAttribute.getIconUrl());
            return Optional.of(attributeDTO);
        }
        return Optional.empty();
    }

    @Override
    public List<AttributeDTO> searchAll() {
        List<Attribute> attributes = attributeRepository.findAll();
        return attributes.stream()
                .map(attribute -> new AttributeDTO(attribute.getAttributeId(), attribute.getName(), attribute.getIconUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AttributeDTO> updateAttribute(int id, AttributeDTO attributeDTO) {
        Optional<Attribute> attributeOpt = attributeRepository.findById(id);
        if (attributeOpt.isPresent()) {
            Attribute existingAttribute = attributeOpt.get();
            if (attributeDTO.getName() != null && !attributeDTO.getName().isEmpty()) {
                existingAttribute.setName(attributeDTO.getName());
            }
            if (attributeDTO.getIconUrl() != null && !attributeDTO.getIconUrl().isEmpty()) {
                existingAttribute.setIconUrl(attributeDTO.getIconUrl());
            }
            attributeRepository.save(existingAttribute);

            return Optional.of(new AttributeDTO(existingAttribute.getAttributeId(), existingAttribute.getName(), existingAttribute.getIconUrl()));
        }
        return Optional.empty();
    }

    @Override
    public void deleteAttribute(Integer id) {
        Optional<Attribute> attributeOpt = attributeRepository.findById(id);
        if (attributeOpt.isPresent()) {
            Attribute attribute = attributeOpt.get();
            // Buscar todas las prendas que contienen este atributo
            List<Clothe> clothesWithAttribute = clotheRepository.findByAttributesContaining(attribute);
            // Marcar todas las prendas asociadas como eliminadas lógicamente
            for (Clothe clothe : clothesWithAttribute) {
                clothe.deleteLogically();
            }
            // Guardar los cambios en las prendas
            clotheRepository.saveAll(clothesWithAttribute);
            // Eliminar el atributo
            attributeRepository.delete(attribute);
        } else {
            throw new RuntimeException("Atributo no encontrado");
        }
    }

}
