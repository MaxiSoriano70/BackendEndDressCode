package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.dto.ReservationDTO;
import com.grupo5.DressCode.service.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private IReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.createReservation(reservationDTO);
        return ResponseEntity.ok(createdReservation);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable int id) {
        Optional<ReservationDTO> reservationDTO = reservationService.searchForId(id);
        return reservationDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.searchAll();
        return ResponseEntity.ok(reservations);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable int id, @RequestBody ReservationDTO reservationDTO) {
        reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confirm-payment")
    public ResponseEntity<Void> confirmReservationPayment(@PathVariable int id) {
        reservationService.confirmReservationPayment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cancel-pending")
    public ResponseEntity<Void> cancelPendingReservations() {
        reservationService.cancelPendingReservations();
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{reservationId}/return-clothe/{clotheId}")
    public ResponseEntity<Void> processReturn(@PathVariable int reservationId, @PathVariable int clotheId) {
        reservationService.processReturn(reservationId, clotheId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{reservationId}/item/{clotheId}")
    public ResponseEntity<Void> removeItem(@PathVariable int reservationId, @PathVariable int clotheId) {
        reservationService.removeItemFromReservation(reservationId, clotheId);
        return ResponseEntity.noContent().build();
    }
}
