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
import com.grupo5.DressCode.utils.EItemReservationStatus;
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
        reservation.setDate(LocalDate.now());
        reservation.setStatus(EReservationStatus.PENDIENTE);
        reservation.setPaid(false);

        Set<ReservationItem> reservationItems = new HashSet<>();
        for (ReservationItemDTO itemDTO : reservationDTO.getItems()) {
            Clothe clothe = clotheRepository.findById(itemDTO.getClotheId())
                    .orElseThrow(() -> new RuntimeException("Clothe no encontrada"));

            if (clothe.isDeleted()) {
                throw new RuntimeException("La prenda " + clothe.getName() + " está eliminada y no puede reservarse.");
            }
            if (!clothe.isActive()) {
                throw new RuntimeException("La prenda " + clothe.getName() + " no está disponible para reservar.");
            }

            ReservationItem item = new ReservationItem();
            item.setClothe(clothe);
            item.setStartDate(itemDTO.getStartDate());
            item.setEndDate(itemDTO.getEndDate());
            item.setPrice(clothe.getPrice());
            item.setRentalDays(item.getRentalDays());
            item.setItemReservationStatus(EItemReservationStatus.RESERVADO);
            item.setReservation(reservation);
            item.calculateSubtotal();

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

    public void updateReservation(int id, ReservationDTO reservationDTO) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservación no encontrada"));

        Set<ReservationItem> currentItems = reservation.getItems(); // Ítems actuales en la reserva

        for (ReservationItemDTO itemDTO : reservationDTO.getItems()) {
            Optional<ReservationItem> existingItemOpt = currentItems.stream()
                    .filter(item -> item.getClothe().getClotheId().equals(itemDTO.getClotheId()))
                    .findFirst();

            if (existingItemOpt.isPresent()) {
                // Si el ítem ya existe, solo lo actualizamos
                ReservationItem existingItem = existingItemOpt.get();
                existingItem.setStartDate(itemDTO.getStartDate());
                existingItem.setEndDate(itemDTO.getEndDate());
                existingItem.setRentalDays(itemDTO.getRentalDays()); // Se actualiza correctamente
                existingItem.calculateSubtotal();
            } else {
                // Si el ítem no existe, lo agregamos como nuevo
                Clothe clothe = clotheRepository.findById(itemDTO.getClotheId())
                        .orElseThrow(() -> new RuntimeException("Prenda no encontrada"));

                if (clothe.isDeleted()) {
                    throw new RuntimeException("La prenda " + clothe.getName() + " está eliminada y no puede reservarse.");
                }
                if (!clothe.isActive()) {
                    throw new RuntimeException("La prenda " + clothe.getName() + " no está disponible para reservar.");
                }

                ReservationItem newItem = new ReservationItem();
                newItem.setClothe(clothe);
                newItem.setStartDate(itemDTO.getStartDate());
                newItem.setEndDate(itemDTO.getEndDate());
                newItem.setPrice(clothe.getPrice());
                newItem.setRentalDays(itemDTO.getRentalDays());
                newItem.setItemReservationStatus(EItemReservationStatus.RESERVADO);
                newItem.setReservation(reservation);
                newItem.calculateSubtotal();
                currentItems.add(newItem); // Se agrega al set de ítems

                // Cambiar el estado de la prenda a inactiva
                clothe.setActive(false);
                clotheRepository.save(clothe);
            }
        }

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
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reservation.isPaid()) {
            reservation.setPaid(true);
        }

        reservation.setStatus(EReservationStatus.CONFIRMADO);

        for (ReservationItem item : reservation.getItems()) {
            item.setItemReservationStatus(EItemReservationStatus.ALQUILADO);
        }

        reservationRepository.save(reservation);
    }

    @Override
    public void cancelPendingReservations() {
        List<Reservation> pendingReservations = reservationRepository.findByStatus(EReservationStatus.PENDIENTE);

        LocalDateTime now = LocalDateTime.now();

        for (Reservation reservation : pendingReservations) {
            LocalDateTime startDateTime = reservation.getDate().atStartOfDay();
            long hoursSinceCreation = java.time.Duration.between(startDateTime, now).toHours();

            if (hoursSinceCreation > 24 && !reservation.isPaid()) {
                reservation.setStatus(EReservationStatus.CANCELADO);

                for (ReservationItem item : reservation.getItems()) {
                    item.setItemReservationStatus(EItemReservationStatus.CANCELADO);
                    Clothe clothe = item.getClothe();
                    clothe.setActive(true);
                    clotheRepository.save(clothe);
                }

                reservationRepository.save(reservation);
            }
        }
    }
    @Override
    public void processReturn(int reservationId, int clotheId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservación no encontrada"));

        ReservationItem item = reservation.getItems().stream()
                .filter(i -> i.getClothe().getClotheId() == clotheId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada en la reserva"));

        LocalDate today = LocalDate.now();
        LocalDateTime endDateTime = item.getEndDate().atStartOfDay();
        LocalDateTime todayStartOfDay = today.atStartOfDay();

        if (todayStartOfDay.isBefore(endDateTime)) {
            item.setDiscount(item.getPrice() * 0.05f);
        } else if (todayStartOfDay.isAfter(endDateTime)) {
            item.setSurcharge(item.getPrice() * 0.15f);
        }

        item.setItemReservationStatus(EItemReservationStatus.DEVUELTO);
        item.getClothe().setActive(true);
        clotheRepository.save(item.getClothe());

        reservation.setRefund(reservation.getTotalDiscount());
        reservation.setSurcharge(reservation.getTotalSurcharge());
        reservation.calculateTotalPrice();

        boolean allReturned = reservation.getItems().stream()
                .allMatch(i -> i.getItemReservationStatus() == EItemReservationStatus.DEVUELTO);

        reservation.setStatus(allReturned ? EReservationStatus.COMPLETADO : EReservationStatus.INCOMPLETO);
        reservationRepository.save(reservation);
    }

    @Override
    public void removeItemFromReservation(int reservationId, int clotheId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservación no encontrada"));

        ReservationItem item = reservation.getItems().stream()
                .filter(i -> i.getClothe().getClotheId() == clotheId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada en la reserva"));

        reservation.getItems().remove(item);
        item.getClothe().setActive(true);
        clotheRepository.save(item.getClothe());

        if (reservation.getItems().isEmpty()) {
            reservation.setStatus(EReservationStatus.CANCELADO);
        }

        reservation.calculateTotalPrice();
        reservationRepository.save(reservation);
    }
}
