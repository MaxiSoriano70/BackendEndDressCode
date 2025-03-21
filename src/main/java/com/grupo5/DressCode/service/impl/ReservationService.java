package com.grupo5.DressCode.service.impl;

import com.grupo5.DressCode.dto.ReservationDTO;
import com.grupo5.DressCode.dto.ReservationItemDTO;
import com.grupo5.DressCode.entity.Clothe;
import com.grupo5.DressCode.entity.Reservation;
import com.grupo5.DressCode.entity.ReservationItem;
import com.grupo5.DressCode.repository.IClotheRepository;
import com.grupo5.DressCode.repository.IReservationItemRepository;
import com.grupo5.DressCode.repository.IReservationRepository;
import com.grupo5.DressCode.security.entity.User;
import com.grupo5.DressCode.security.repository.IUserRepository;
import com.grupo5.DressCode.service.IReservationService;
import com.grupo5.DressCode.utils.EReservationStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ReservationService implements IReservationService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IReservationRepository reservationRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IClotheRepository clotheRepository;
    @Autowired
    private IReservationItemRepository reservationItemRepository;

    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        User user = userRepository.findById(reservationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User no encontrado"));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setStartDate(reservationDTO.getStartDate());
        reservation.setEndDate(reservationDTO.getEndDate());
        reservation.setStatus(EReservationStatus.PENDIENTE);
        reservation.setPaid(false);

        long rentalDays = java.time.Duration.between(
                reservation.getStartDate().atStartOfDay(),
                reservation.getEndDate().atStartOfDay()
        ).toDays();

        Set<ReservationItem> reservationItems = new HashSet<>();
        for (ReservationItemDTO itemDTO : reservationDTO.getItems()) {
            Clothe clothe = clotheRepository.findById(itemDTO.getClotheId())
                    .orElseThrow(() -> new RuntimeException("Clothe no encontrada"));

            // Validaciones de eliminación lógica y disponibilidad
            if (clothe.isDeleted()) {
                throw new RuntimeException("La prenda " + clothe.getName() + " está eliminada y no puede reservarse.");
            }
            if (!clothe.isActive()) {
                throw new RuntimeException("La prenda " + clothe.getName() + " no está disponible para reservar.");
            }

            ReservationItem item = new ReservationItem();
            item.setClothe(clothe);
            item.setPrice(clothe.getPrice());
            item.setRentalDays((int) rentalDays);
            item.setReservation(reservation);

            reservationItems.add(item);

            clothe.setActive(false);
            clotheRepository.save(clothe);
        }

        reservation.setItems(reservationItems);
        reservation.calculateTotalPrice();

        Reservation savedReservation = reservationRepository.save(reservation);

        return modelMapper.map(savedReservation, ReservationDTO.class);
    }


    @Override
    public Optional<ReservationDTO> searchForId(int id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.map(res -> modelMapper.map(res, ReservationDTO.class));
    }

    @Override
    public List<ReservationDTO> searchAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDTO.class))
                .toList();
    }

    @Override
    public void updateReservation(int id, ReservationDTO reservationDTO) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservationDTO.getStartDate() != null) {
            reservation.setStartDate(reservationDTO.getStartDate());
        }
        if (reservationDTO.getEndDate() != null) {
            reservation.setEndDate(reservationDTO.getEndDate());
        }
        if (reservationDTO.getStatus() != null) {
            reservation.setStatus(EReservationStatus.valueOf(reservationDTO.getStatus().toString()));
        }
        reservation.setPaid(reservationDTO.isPaid());

        long rentalDays = java.time.Duration.between(
                reservation.getStartDate().atStartOfDay(),
                reservation.getEndDate().atStartOfDay()
        ).toDays();

        Set<ReservationItem> updatedItems = new HashSet<>();
        Set<Integer> updatedItemIds = new HashSet<>();
        Set<Clothe> removedClothes = new HashSet<>(reservation.getItems().stream().map(ReservationItem::getClothe).toList());

        for (ReservationItemDTO itemDTO : reservationDTO.getItems()) {
            Optional<ReservationItem> existingItemOpt = reservation.getItems().stream()
                    .filter(item -> item.getClothe().getClotheId().equals(itemDTO.getClotheId()))
                    .findFirst();

            if (existingItemOpt.isPresent()) {
                ReservationItem existingItem = existingItemOpt.get();
                existingItem.setPrice(itemDTO.getPrice() != null ? itemDTO.getPrice() : existingItem.getPrice());
                existingItem.setRentalDays((int) rentalDays);
                updatedItems.add(existingItem);
                updatedItemIds.add(existingItem.getIdReservationItem());

                removedClothes.remove(existingItem.getClothe());
            } else {
                Clothe clothe = clotheRepository.findById(itemDTO.getClotheId())
                        .orElseThrow(() -> new RuntimeException("Clothe not found"));

                if (clothe.isDeleted()) {
                    throw new RuntimeException("La prenda " + clothe.getName() + " está eliminada y no puede reservarse.");
                }
                if (!clothe.isActive()) {
                    throw new RuntimeException("La prenda " + clothe.getName() + " no está disponible para reservar.");
                }

                clothe.setActive(false);
                clotheRepository.save(clothe);

                ReservationItem newItem = new ReservationItem();
                newItem.setClothe(clothe);
                newItem.setPrice(clothe.getPrice());
                newItem.setRentalDays((int) rentalDays);
                newItem.setReservation(reservation);

                updatedItems.add(newItem);
            }
        }

        reservation.getItems().removeIf(item -> {
            boolean shouldRemove = !updatedItemIds.contains(item.getIdReservationItem());
            if (shouldRemove) {
                item.setReservation(null);
                removedClothes.add(item.getClothe());
            }
            return shouldRemove;
        });

        for (Clothe removedClothe : removedClothes) {
            removedClothe.setActive(true);
            clotheRepository.save(removedClothe);
        }

        reservation.getItems().clear();
        reservation.getItems().addAll(updatedItems);

        reservation.calculateTotalPrice();

        reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Integer id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservación no encontrada"));

        for (ReservationItem item : reservation.getItems()) {
            Clothe clothe = item.getClothe();
            clothe.setActive(true);
            clotheRepository.save(clothe);
        }

        reservationRepository.delete(reservation);
    }

    @Override
    public void confirmReservationPayment(int reservationId) {
        // Obtener la reserva por ID
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Verificar si la reserva ya ha sido pagada
        if (!reservation.isPaid()) {
            // Si no está pagada, marcar el pago como verdadero
            reservation.setPaid(true);
        }

        // Cambiar el estado de la reserva a CONFIRMADO
        reservation.setStatus(EReservationStatus.CONFIRMADO);

        // Guardar la reserva actualizada
        reservationRepository.save(reservation);
    }

    @Override
    public void cancelPendingReservations() {
        // Obtener las reservas con estado PENDIENTE
        List<Reservation> pendingReservations = reservationRepository.findByStatus(EReservationStatus.PENDIENTE);

        // Obtener la fecha y hora actual
        LocalDateTime now = LocalDateTime.now();

        for (Reservation reservation : pendingReservations) {
            // Convertir la fecha de inicio a LocalDateTime (asumiendo que es LocalDate)
            LocalDateTime startDateTime = reservation.getStartDate().atStartOfDay();

            // Calcular la duración en horas desde la fecha de inicio
            long hoursSinceCreation = java.time.Duration.between(startDateTime, now).toHours();

            // Solo cancelar si han pasado más de 24 horas y la reserva no está pagada
            if (hoursSinceCreation > 24 && !reservation.isPaid()) {
                // Cambiar el estado de la reserva a CANCELADO
                reservation.setStatus(EReservationStatus.CANCELADO);

                // Reactivar las prendas asociadas a la reserva
                for (ReservationItem item : reservation.getItems()) {
                    Clothe clothe = item.getClothe();
                    clothe.setActive(true);
                    clotheRepository.save(clothe);
                }

                // Guardamos el estado actualizado de la reserva
                reservationRepository.save(reservation);
            }
        }
    }
    @Override
    public void processReturn(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservación no encontrada"));

        // Verificar que el estado de la reserva sea "CONFIRMADO"
        if (reservation.getStatus() != EReservationStatus.CONFIRMADO) {
            throw new RuntimeException("La reservación no está confirmada para ser procesada.");
        }

        // Verificar que el pago haya sido realizado (isPaid == true)
        if (!reservation.isPaid()) {
            throw new RuntimeException("La reservación no ha sido pagada. No se puede procesar la devolución.");
        }

        LocalDate today =
                LocalDate.now
                        ();
        reservation.setReturnDate(today);

        // Convertir las fechas a LocalDateTime con inicio del día (00:00:00) para comparación precisa
        LocalDateTime endDateTime = reservation.getEndDate().atStartOfDay(); // Fin de la reserva a las 00:00:00
        LocalDateTime todayStartOfDay = today.atStartOfDay(); // Hoy a las 00:00:00

        // Si la devolución se realiza antes de la fecha de fin, aplicar un descuento del 10%
        if (todayStartOfDay.isBefore(endDateTime)) {
            float discount = reservation.getTotalPrice() * 0.10f; // 10% descuento
            reservation.setTotalPrice(reservation.getTotalPrice() - discount);
        }
        // Si la devolución se realiza después de la fecha de fin, aplicar un recargo del 15%
        else if (todayStartOfDay.isAfter(endDateTime)) {
            float surcharge = reservation.getTotalPrice() * 0.15f; // 15% recargo
            reservation.setTotalPrice(reservation.getTotalPrice() + surcharge);
        }

        // Si se devuelve en la fecha pactada, el precio permanece igual
        // No es necesario modificar el precio en este caso.

        // Reactivar las prendas asociadas a la reserva
        for (ReservationItem item : reservation.getItems()) {
            Clothe clothe = item.getClothe();
            clothe.setActive(true);

            clotheRepository.save
                    (clothe);
        }

        // Cambiar el estado de la reserva a COMPLETADO
        reservation.setStatus(EReservationStatus.COMPLETADO);

        reservationRepository.save
                (reservation);
    }

}
