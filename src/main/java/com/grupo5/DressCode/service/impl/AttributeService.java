package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.entity.Attribute;
import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.repository.IAttributeRepository;
import com.grupo5.DressCode.repository.IClotheRepository;
import com.grupo5.DressCode.service.IAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttributeService implements IAttributeService {
    private final IAttributeRepository attributeRepository;

    @Autowired
    private IClotheRepository clotheRepository;

    public AttributeService(IAttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    @Override
    public Attribute createAttribute(Attribute attribute){
        return  attributeRepository.save(attribute);
    }

    @Override
    public Optional<Attribute> searchForId(int id) {
        return attributeRepository.findById(id);
    }

    @Override
    public List<Attribute> searchAll(){
        return attributeRepository.findAll();
    }

    @Override
    public Optional<Attribute> updateAttribute(int id, Attribute attribute) {
        Optional<Attribute> AttributeOpt = attributeRepository.findById(id);
        if (AttributeOpt.isPresent()) {
            Attribute existingAttribute = AttributeOpt.get();
            if (attribute.getName() != null && !attribute.getName().isEmpty()) {
                existingAttribute.setName(attribute.getName());
            }
            if (attribute.getIconUrl() != null && !attribute.getIconUrl().isEmpty()) {
                existingAttribute.setIconUrl(attribute.getIconUrl());
            }
            attributeRepository.save(existingAttribute);
        }
        return AttributeOpt;
    }

    @Override
    public void deleteAttribute(Integer id) {
        Optional<Attribute> attributeOpt = attributeRepository.findById(id);
        if (attributeOpt.isPresent()) {
            Attribute attribute = attributeOpt.get();

            // Buscar todas las prendas que contienen este atributo
            List<Clothe> clothesWithAttribute = clotheRepository.findByAttributesContaining(attribute);

            // Eliminar la referencia en cada prenda
            for (Clothe clothe : clothesWithAttribute) {
                clothe.getAttributes().remove(attribute);
            }

            // Guardar los cambios en las prendas antes de eliminar el atributo
            clotheRepository.saveAll(clothesWithAttribute);

            // Ahora sí, eliminar el atributo
            attributeRepository.delete(attribute);
        }
    }
}
