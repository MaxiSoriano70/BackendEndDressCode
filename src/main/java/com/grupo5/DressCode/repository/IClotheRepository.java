package com.grupo5.DressCode.repository;

import com.grupo5.DressCode.entity.Clothe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClotheRepository extends JpaRepository<Clothe, Integer> {
}
