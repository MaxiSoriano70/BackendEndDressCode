package com.grupo5.DressCode.repository;

import com.grupo5.DressCode.entity.Attribute;
import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.entity.Category;
import com.grupo5.DressCode.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IClotheRepository extends JpaRepository<Clothe, Integer> {
    List<Clothe> findByAttributesContaining(Attribute attribute);
    List<Clothe> findByCategory(Category category);
    List<Clothe> findByColor(Color color);
    Optional<Clothe> findByClotheIdAndIsDeletedFalse(Integer clotheId);
}
