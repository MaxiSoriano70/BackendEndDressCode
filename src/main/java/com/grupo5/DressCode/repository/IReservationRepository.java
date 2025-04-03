package com.grupo5.DressCode.repository;

import com.grupo5.DressCode.entity.Reservation;
import com.grupo5.DressCode.utils.EReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByStatus(EReservationStatus status);
    List<Reservation> findByUser_UsuarioId(int usuarioId);
    boolean existsByUserUsuarioIdAndItemsClotheClotheId(int usuarioId, int clotheId);
}
