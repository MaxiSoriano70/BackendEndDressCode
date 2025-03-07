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
    private String description;
    private String size;
    private String name;
    private Double price;
    private int stock;
    private boolean active;
    private int categoryID;
    private int colorID;

}