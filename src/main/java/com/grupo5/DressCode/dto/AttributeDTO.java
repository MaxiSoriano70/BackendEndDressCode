package com.grupo5.DressCode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDTO {
    private Integer attributeId;
    private String name;
    private String iconUrl;
}
