package com.grupo5.DressCode.repository;

import com.grupo5.DressCode.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IColorRepository extends JpaRepository<Color, Integer> {
}
