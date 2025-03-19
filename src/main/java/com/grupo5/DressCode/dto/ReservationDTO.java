package com.grupo5.DressCode.dto;

import com.grupo5.DressCode.utils.EReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Integer reservationId;
    private Integer userId;
    private Set<ReservationItemDTO> items;
    private LocalDate startDate;
    private LocalDate endDate;
    private Float totalPrice;
    private EReservationStatus status;
    private boolean isPaid;
}
