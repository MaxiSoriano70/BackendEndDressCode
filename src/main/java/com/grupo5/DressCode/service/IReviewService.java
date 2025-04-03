package com.grupo5.DressCode.service;

import com.grupo5.DressCode.dto.ReviewDTO;

import java.util.List;
import java.util.Optional;

public interface IReviewService {
    ReviewDTO createReview(ReviewDTO reviewDTO);
    Optional<ReviewDTO> searchForId(int id);
    List<ReviewDTO> searchAll();
    void updateReview(int id, ReviewDTO reviewDTO);
    void deleteReview(Integer id);
    List<ReviewDTO> getReviewsByClotheId(int clotheId);
    List<ReviewDTO> getReviewsByUserId(int userId);
    int getAverageRatingByClotheId(int clotheId);
}
