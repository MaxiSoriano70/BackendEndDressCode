package com.grupo5.DressCode.repository;

import com.grupo5.DressCode.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageRepository extends JpaRepository<Image, Integer> {
}
