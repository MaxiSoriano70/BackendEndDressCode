package com.grupo5.DressCode.dto;

import com.grupo5.DressCode.utils.ESize;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClotheDTO {
    private Integer clotheId;
    private String sku;
    private String description;
    private ESize size;
    private String name;
    private Float price;
    private Integer stock;
    private boolean active;
    private Integer categoryId;
    private Integer colorId;
    private Set<String> imageUrls;
    private Set<Integer> attributeIds;
    private boolean isDeleted;
}
