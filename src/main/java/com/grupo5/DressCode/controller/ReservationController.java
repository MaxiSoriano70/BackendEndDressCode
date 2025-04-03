package com.grupo5.DressCode.controller;

import com.grupo5.DressCode.dto.ReservationDTO;
import com.grupo5.DressCode.service.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> createReservation(@RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.createReservation(reservationDTO);
        if (createdReservation != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Reservación creada con éxito\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error al crear la reservación\"}");
        }
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
    public ResponseEntity<String> updateReservation(@PathVariable int id, @RequestBody ReservationDTO reservationDTO) {
        boolean updated = reservationService.updateReservation(id, reservationDTO);
        if (updated) {
            return ResponseEntity.ok("{\"message\": \"Reservación editada con éxito\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error al editar la reserva\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confirm-payment")
    public ResponseEntity<String> confirmReservationPayment(@PathVariable int id) {
        boolean confirmed = reservationService.confirmReservationPayment(id);
        if (confirmed) {
            return ResponseEntity.ok("{\"message\": \"Reservación pagada con éxito\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error en el pago de la reservación\"}");
        }
    }

    @DeleteMapping("/cancel-pending")
    public ResponseEntity<Void> cancelPendingReservations() {
        reservationService.cancelPendingReservations();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{reservationId}/return-clothe/{clotheId}")
    public ResponseEntity<String> processReturn(@PathVariable int reservationId, @PathVariable int clotheId) {
        boolean returned = reservationService.processReturn(reservationId, clotheId);
        if (returned) {
            return ResponseEntity.ok("{\"message\": \"Prenda devuelta con éxito\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error al devolver prenda\"}");
        }
    }

    @DeleteMapping("/{reservationId}/item/{clotheId}")
    public ResponseEntity<String> removeItem(@PathVariable int reservationId, @PathVariable int clotheId) {
        boolean removed = reservationService.removeItemFromReservation(reservationId, clotheId);
        if (removed) {
            return ResponseEntity.ok("{\"message\": \"Ítem eliminado con éxito\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Error al eliminar ítem\"}");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByUser(@PathVariable int userId) {
        List<ReservationDTO> reservations = reservationService.searchAllFromUser(userId);
        return ResponseEntity.ok(reservations);
    }
}
