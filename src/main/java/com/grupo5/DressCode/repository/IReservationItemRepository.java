package com.grupo5.DressCode.repository;

import com.grupo5.DressCode.entity.ReservationItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IReservationItemRepository extends JpaRepository<ReservationItem, Integer> {
    List<ReservationItem> findByClothe_ClotheId(Integer clotheId);
}
