package com.grupo5.DressCode.entity;

import com.grupo5.DressCode.utils.ESize;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ESize size;

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

    @ManyToMany
    @JoinTable(
            name = "clothe_images",
            joinColumns = @JoinColumn(name = "clothe_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private Set<Image> images;
}
