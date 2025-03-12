package com.grupo5.DressCode.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClothesDTO {
    private String sku;
    private String description;
    private String size;
    private String name;
    private Double price;
    private int stock;
    private boolean active;
    private int categoryID;
    private int colorID;
    // Campo para asociar imágenes existentes por su ID.
    private List<Integer> imageIds;
    private List<Integer> attributeIds;
}
