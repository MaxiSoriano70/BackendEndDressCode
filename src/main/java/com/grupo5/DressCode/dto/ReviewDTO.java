package com.grupo5.DressCode.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {

    private Integer reviewId;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Integer userId;

    @NotNull(message = "El ID de la prenda no puede ser nulo")
    private Integer clotheId;

    @NotBlank(message = "El comentario no puede estar vacío")
    private String comment;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer rating;
}
