package com.grupo5.DressCode.service;

import com.grupo5.DressCode.dto.ReservationDTO;

import java.util.List;
import java.util.Optional;

public interface IReservationService {
    ReservationDTO createReservation(ReservationDTO ReservationDTO);
    Optional<ReservationDTO> searchForId(int id);
    List<ReservationDTO> searchAll();
    void updateReservation(int id, ReservationDTO reservationDTO);
    void deleteReservation(Integer id);
    void confirmReservationPayment(int reservationId);
    void cancelPendingReservations();
    void processReturn(int reservationId);
}
