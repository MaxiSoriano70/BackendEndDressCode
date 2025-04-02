package com.grupo5.DressCode.entity;

import com.grupo5.DressCode.security.entity.User;
import com.grupo5.DressCode.utils.EReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReservationItem> items = new HashSet<>();

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Float totalPrice = 0f;

    @Column(nullable = true)
    private Float surcharge = 0f;

    @Column(nullable = true)
    private Float refund = 0f;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EReservationStatus status;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isPaid;

    public void calculateTotalPrice() {
        float itemsTotal = items.stream()
                .map(ReservationItem::getSubtotal)
                .reduce(0f, Float::sum);

        this.totalPrice = itemsTotal + surcharge - refund;
    }
    public Float getTotalDiscount() {
        return items.stream().map(ReservationItem::getDiscount).reduce(0f, Float::sum);
    }

    public Float getTotalSurcharge() {
        return items.stream().map(ReservationItem::getSurcharge).reduce(0f, Float::sum);
    }
}
