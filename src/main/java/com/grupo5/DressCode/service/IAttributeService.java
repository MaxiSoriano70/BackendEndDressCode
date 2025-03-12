package com.grupo5.DressCode.service;

import com.grupo5.DressCode.entity.Attribute;
import com.grupo5.DressCode.security.entity.User;

import java.util.List;
import java.util.Optional;

public interface IAttributeService {
    Attribute createAttribute(Attribute attribute);
    Optional<Attribute> searchForId(int id);
    List<Attribute> searchAll();
    Optional<Attribute> updateAttribute(int id, Attribute attribute);
    void deleteAttribute(Integer id);
}
