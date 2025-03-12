package com.grupo5.DressCode.repository;

import com.grupo5.DressCode.entity.Attribute;
import com.grupo5.DressCode.entity.Clothe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IClotheRepository extends JpaRepository<Clothe, Integer> {
    List<Clothe> findByAttributesContaining(Attribute attribute);
}
