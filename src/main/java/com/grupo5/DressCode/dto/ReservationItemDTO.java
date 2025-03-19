package com.grupo5.DressCode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationItemDTO {
    private Integer idReservationItem;
    private Integer clotheId;
    private String clotheName;
    private Float price;
    private Float subtotal;
    private Integer rentalDays;
}

