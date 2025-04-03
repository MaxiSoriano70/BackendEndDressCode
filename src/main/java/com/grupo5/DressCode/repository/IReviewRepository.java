package com.grupo5.DressCode.repository;

import com.grupo5.DressCode.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IReviewRepository extends JpaRepository<Review, Integer> {
    boolean existsByUserUsuarioIdAndClotheClotheId(int usuarioId, int clotheId);
    List<Review> findByClotheClotheId(int clotheId);
    List<Review> findByUserUsuarioId(int userId);

}
