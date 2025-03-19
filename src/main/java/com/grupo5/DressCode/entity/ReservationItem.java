package com.grupo5.DressCode.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation_items")
public class ReservationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReservationItem;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "clothe_id", nullable = false)
    private Clothe clothe;

    @Column(nullable = false)
    private Integer rentalDays;

    @Column(nullable = false)
    private Float price;

    public Float getSubtotal() {
        return this.rentalDays * this.price;
    }
}
