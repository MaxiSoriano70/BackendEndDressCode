package com.grupo5.DressCode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClothesDTO {
    private Integer clotheId;
    private String sku;
    private String description;
    private String size;
    private String name;
    private Double price;
    private int stock;
    private boolean active;
    private String categoryName;
    private String colorName;
    private Set<String> imageUrls; // URLs de las imágenes
}
