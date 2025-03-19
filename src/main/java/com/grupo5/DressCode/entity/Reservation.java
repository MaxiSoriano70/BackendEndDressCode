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
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = true)
    private LocalDate returnDate;

    @Column(nullable = false)
    private Float totalPrice = 0f;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EReservationStatus status;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isPaid;

    public void calculateTotalPrice() {
        this.totalPrice = items.stream()
                .map(ReservationItem::getSubtotal)
                .reduce(0f, Float::sum);
    }
}
