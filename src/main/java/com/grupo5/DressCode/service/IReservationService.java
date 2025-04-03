package com.grupo5.DressCode.service;

import com.grupo5.DressCode.dto.ReservationDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IReservationService {
    ReservationDTO createReservation(ReservationDTO ReservationDTO);
    Optional<ReservationDTO> searchForId(int id);
    List<ReservationDTO> searchAll();
    boolean updateReservation(int id, ReservationDTO reservationDTO);
    boolean deleteReservation(Integer id);
    boolean confirmReservationPayment(int reservationId);
    void cancelPendingReservations();
    boolean processReturn(int reservationId, int clotheId);
    boolean removeItemFromReservation(int reservationId, int clotheId);
    List<ReservationDTO> searchAllFromUser(int userId);
    List<LocalDate> getReservedDatesForClothe(int clotheId);
}
