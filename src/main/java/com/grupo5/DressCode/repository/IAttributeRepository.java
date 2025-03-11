package com.grupo5.DressCode.repository;

import com.grupo5.DressCode.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAttributeRepository extends JpaRepository<Attribute, Integer> {
}
