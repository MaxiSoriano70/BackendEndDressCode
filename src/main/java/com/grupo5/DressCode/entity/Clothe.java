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

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ESize size;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    @Positive
    private Float price;

    @Column(nullable = false)
    @Min(0)
    private Integer stock;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = true)
    private Color color;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "clothe_id")
    private Set<Image> images = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "clothe_attribute",
            joinColumns = @JoinColumn(name = "clothe_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private Set<Attribute> attributes = new HashSet<>();

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted;

    public void deleteLogically() {
        this.isDeleted = true;
    }
}
