package com.grupo5.DressCode.entity;

import com.grupo5.DressCode.utils.ESize;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "CLOTHES")
public class Clothe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clotheId;

    @Column(nullable = false, unique = true)
    @NotNull
    private String sku;

    @Column(nullable = false)
    @NotNull
    private String description;

    // Puedes seguir usando el enum ESize si lo prefieres; aquí se usa String para simplificar.
    @Column(nullable = false)
    @NotNull
    private String size;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    @Positive
    private float price;

    @Column(nullable = false)
    @Min(0)
    private Integer stock;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    // Relación uno a muchos: una prenda puede tener varias imágenes.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "clothe_id") // Esta columna se agregará en la tabla IMAGES
    private Set<Image> images = new HashSet<>();
}
