package com.grupo5.DressCode.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "COLORS")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer colorId;
    @Column(nullable = false)
    private String name;
    @Version
    @Column(nullable = false)
    private Integer version;
}
