package com.grupo5.DressCode.entity;

import com.grupo5.DressCode.utils.EItemReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

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
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = true)
    private LocalDate returnDate;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private Integer rentalDays;

    @Column(nullable = false)
    private Float subtotal = 0.0f;

    @Column(nullable = true)
    private Float discount = 0.0f;

    @Column(nullable = true)
    private Float surcharge = 0.0f;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EItemReservationStatus itemReservationStatus;

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio.");
        }
    }

    public Integer getRentalDays() {
        if (startDate != null && endDate != null) {
            return (int) Duration.between(
                    startDate.atStartOfDay(),
                    endDate.atStartOfDay()
            ).toDays();
        }
        return 0;
    }

    public void calculateSubtotal() {
        this.rentalDays = getRentalDays();
        this.subtotal = (this.rentalDays * this.price) - this.discount + this.surcharge;
    }
}
