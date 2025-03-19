package com.grupo5.DressCode.repository;

import com.grupo5.DressCode.entity.ReservationItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservationItemRepository extends JpaRepository<ReservationItem, Integer> {
}
