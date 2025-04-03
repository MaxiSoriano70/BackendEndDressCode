package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.dto.ReviewDTO;
import com.grupo5.DressCode.entity.Review;
import com.grupo5.DressCode.repository.IClotheRepository;
import com.grupo5.DressCode.repository.IReservationRepository;
import com.grupo5.DressCode.repository.IReviewRepository;
import com.grupo5.DressCode.security.repository.IUserRepository;
import com.grupo5.DressCode.service.IReviewService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService implements IReviewService {
    @Autowired
    private IReviewRepository reviewRepository;
    @Autowired
    private IReservationRepository reservationRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IClotheRepository clotheRepository;
    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        var user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        var clothe = clotheRepository.findById(reviewDTO.getClotheId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prenda no encontrada"));

        boolean hasRented = reservationRepository.existsByUserUsuarioIdAndItemsClotheClotheId(reviewDTO.getUserId(), reviewDTO.getClotheId());

        if (!hasRented) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no ha alquilado esta prenda.");
        }

        boolean alreadyReviewed = reviewRepository.existsByUserUsuarioIdAndClotheClotheId(
                reviewDTO.getUserId(), reviewDTO.getClotheId());

        if (alreadyReviewed) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya ha dejado una reseña para esta prenda.");
        }

        Review review = new Review();
        review.setUser(user);
        review.setClothe(clothe);
        review.setComment(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());

        review = reviewRepository.save(review);

        return new ReviewDTO(
                review.getReviewId(),
                review.getUser().getUsuarioId(),
                review.getClothe().getClotheId(),
                review.getComment(),
                review.getRating()
        );
    }
    @Override
    public Optional<ReviewDTO> searchForId(int id) {
        return reviewRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public List<ReviewDTO> searchAll() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateReview(int id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reseña no encontrada"));

        if (reviewDTO.getComment() != null && !reviewDTO.getComment().isEmpty()) {
            review.setComment(reviewDTO.getComment());
        }
        if (reviewDTO.getRating() != null) {
            review.setRating(reviewDTO.getRating());
        }

        reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Integer id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reseña no encontrada"));
        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewDTO> getReviewsByClotheId(int clotheId) {
        return reviewRepository.findByClotheClotheId(clotheId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(int userId) {
        return reviewRepository.findByUserUsuarioId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int getAverageRatingByClotheId(int clotheId) {
        List<Review> reviews = reviewRepository.findByClotheClotheId(clotheId);

        if (reviews.isEmpty()) {
            return 0; // Si no hay reseñas, el promedio es 0.
        }

        double average = reviews.stream()
                .mapToInt(Review::getRating) // Extrae los ratings
                .average() // Calcula el promedio
                .orElse(0); // Si no hay datos, retorna 0

        return (int) Math.round(average); // Redondea según lo especificado
    }

    private ReviewDTO convertToDTO(Review review) {
        return new ReviewDTO(
                review.getReviewId(),
                review.getUser().getUsuarioId(),
                review.getClothe().getClotheId(),
                review.getComment(),
                review.getRating()
        );
    }
}
