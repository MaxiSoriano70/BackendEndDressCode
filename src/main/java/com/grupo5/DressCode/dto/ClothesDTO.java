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
<<<<<<< HEAD:src/main/java/com/grupo5/DressCode/dto/ClothesDTO.java
    private String categoryName;
    private String colorName;
    private Set<String> imageUrls; // URLs de las imágenes
=======
    private int categoryID;
    private int colorID;
    // Campo para asociar imágenes existentes por su ID.
    private List<Integer> imageIds;
    private List<Integer> attributeIds;
>>>>>>> 0f78912d141b9fb735672301d24714e4ff8e32ef:src/main/java/com/grupo5/DressCode/security/dto/ClothesDTO.java
}
