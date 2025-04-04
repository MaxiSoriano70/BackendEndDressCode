package com.grupo5.DressCode.dto;

import com.grupo5.DressCode.utils.EItemReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationItemDTO {
    private Integer idReservationItem;
    private Integer parentReservationId;
    private Integer clotheId;
    private String clotheName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate returnDate;
    private Float price;
    private Integer rentalDays;
    private Float subTotal;
    private Float discount;
    private Float surcharge;
    private EItemReservationStatus itemReservationStatus;
}
