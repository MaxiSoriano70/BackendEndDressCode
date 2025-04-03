package com.grupo5.DressCode.entity;

import com.grupo5.DressCode.security.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "clothe_id", nullable = false)
    private Clothe clothe;

    @Column(nullable = false)
    @NotBlank(message = "El comentario no puede estar vacío")
    private String comment;

    @Column(nullable = false)
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer rating;
}
