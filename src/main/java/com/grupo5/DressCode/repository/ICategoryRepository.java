package com.grupo5.DressCode.repository;

import com.grupo5.DressCode.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
}
