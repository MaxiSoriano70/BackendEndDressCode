package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.dto.ReviewDTO;
import com.grupo5.DressCode.service.IReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> crearReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO reviewARetornar = reviewService.createReview(reviewDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewARetornar);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado.");
        }
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> traerTodos() {
        return ResponseEntity.ok(reviewService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> buscarReviewPorId(@PathVariable Integer id) {
        Optional<ReviewDTO> review = reviewService.searchForId(id);
        if (review.isPresent()) {
            return ResponseEntity.ok(review.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarReview(@PathVariable Integer id, @RequestBody ReviewDTO reviewDTO) {
        try {
            reviewService.updateReview(id, reviewDTO);
            return ResponseEntity.ok("{\"message\": \"Review modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarReview(@PathVariable Integer id) {
        Optional<ReviewDTO> reviewOptional = reviewService.searchForId(id);
        if (reviewOptional.isPresent()) {
            reviewService.deleteReview(id);
            return ResponseEntity.ok("{\"message\": \"Review eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"Review no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/clothe/{clotheId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByClotheId(@PathVariable int clotheId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByClotheId(clotheId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@PathVariable int userId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/clothe/{clotheId}/rating")
    public ResponseEntity<Integer> getAverageRatingByClotheId(@PathVariable int clotheId) {
        int averageRating = reviewService.getAverageRatingByClotheId(clotheId);
        return ResponseEntity.ok(averageRating);
    }
}
