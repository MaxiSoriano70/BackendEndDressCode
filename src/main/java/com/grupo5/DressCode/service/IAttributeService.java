package com.grupo5.DressCode.service;

import com.grupo5.DressCode.dto.AttributeDTO;
import com.grupo5.DressCode.entity.Attribute;
import com.grupo5.DressCode.security.entity.User;

import java.util.List;
import java.util.Optional;

public interface IAttributeService {
    AttributeDTO createAttribute(AttributeDTO attribute);
    Optional<AttributeDTO> searchForId(int id);
    List<AttributeDTO> searchAll();
    Optional<AttributeDTO> updateAttribute(int id, AttributeDTO attribute);
    void deleteAttribute(Integer id);
}
