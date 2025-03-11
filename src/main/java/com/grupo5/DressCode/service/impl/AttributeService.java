package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.entity.Attribute;
import com.grupo5.DressCode.repository.IAttributeRepository;
import com.grupo5.DressCode.security.entity.User;
import com.grupo5.DressCode.service.IAttributeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttributeService implements IAttributeService {
    private final IAttributeRepository attributeRepository;
    public AttributeService(IAttributeRepository attributeRepository){
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
    public void deleteAttribute(Integer id){
        attributeRepository.deleteById(id);
    }
}
