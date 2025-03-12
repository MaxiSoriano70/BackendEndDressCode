package com.grupo5.DressCode.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ATTRIBUTES")
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attributeId;

    @Column(nullable = false, unique = true)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    private String iconUrl;

}
