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
        // Obtener el usuario
        User user = userRepository.findById(reservationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User no encontrado"));

        // Crear una nueva entidad Reservation
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setStartDate(reservationDTO.getStartDate());
        reservation.setEndDate(reservationDTO.getEndDate());
        reservation.setStatus(EReservationStatus.PENDIENTE);
        reservation.setPaid(false);

        // Calcular la cantidad de días de alquiler
        long rentalDays = java.time.Duration.between(reservation.getStartDate().atStartOfDay(), reservation.getEndDate().atStartOfDay()).toDays();

        // Agregar los items a la reserva
        Set<ReservationItem> reservationItems = new HashSet<>();
        for (ReservationItemDTO itemDTO : reservationDTO.getItems()) {
            Clothe clothe = clotheRepository.findById(itemDTO.getClotheId())
                    .orElseThrow(() -> new RuntimeException("Clothe no encontrada"));

            ReservationItem item = new ReservationItem();
            item.setClothe(clothe);
            item.setPrice(clothe.getPrice()); // Precio de la prenda
            item.setRentalDays((int) rentalDays); // Establecer la cantidad de días de alquiler
            item.setReservation(reservation);

            reservationItems.add(item);
        }

        // Establecer los items en la reserva
        reservation.setItems(reservationItems);

        // Calcular el precio total de la reserva
        reservation.calculateTotalPrice();

        // Guardar la reserva en la base de datos
        Reservation savedReservation = reservationRepository.save(reservation);

        // Convertir la entidad a DTO y devolver
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

        // Actualizar los datos básicos de la reserva
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

        // Calcular los días de alquiler
        long rentalDays = java.time.Duration.between(
                reservation.getStartDate().atStartOfDay(),
                reservation.getEndDate().atStartOfDay()
        ).toDays();

        Set<ReservationItem> updatedItems = new HashSet<>();
        Set<Integer> updatedItemIds = new HashSet<>();

        // Procesar los items del DTO
        for (ReservationItemDTO itemDTO : reservationDTO.getItems()) {
            Optional<ReservationItem> existingItemOpt = reservation.getItems().stream()
                    .filter(item -> item.getClothe().getClotheId().equals(itemDTO.getClotheId()))
                    .findFirst();

            if (existingItemOpt.isPresent()) {
                // Si el item ya existe, actualiza los valores
                ReservationItem existingItem = existingItemOpt.get();
                existingItem.setPrice(itemDTO.getPrice() != null ? itemDTO.getPrice() : existingItem.getPrice());
                existingItem.setRentalDays((int) rentalDays);
                updatedItems.add(existingItem);
                updatedItemIds.add(existingItem.getIdReservationItem());
            } else {
                // Si el item no existe, crea uno nuevo y asegúrate de establecer la relación con Reservation
                Clothe clothe = clotheRepository.findById(itemDTO.getClotheId())
                        .orElseThrow(() -> new RuntimeException("Clothe not found"));

                ReservationItem newItem = new ReservationItem();
                newItem.setClothe(clothe);
                newItem.setPrice(clothe.getPrice());
                newItem.setRentalDays((int) rentalDays);
                newItem.setReservation(reservation); // Asegura la relación con la reserva

                updatedItems.add(newItem);
            }
        }

        // Eliminar los items que ya no están en la lista del DTO
        reservation.getItems().removeIf(item -> {
            boolean shouldRemove = !updatedItemIds.contains(item.getIdReservationItem());
            if (shouldRemove) {
                item.setReservation(null);  // Evita problemas de integridad referencial
            }
            return shouldRemove;
        });

        // Asignar los items actualizados
        reservation.getItems().clear();
        reservation.getItems().addAll(updatedItems);

        // Calcular el precio total de la reserva
        reservation.calculateTotalPrice();

        // Guardar la reserva actualizada
        reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Integer id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservacion no encontrada"));
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

        // Recorremos todas las reservas pendientes
        for (Reservation reservation : pendingReservations) {
            // Convertir la fecha de inicio a LocalDateTime (asumiendo que es LocalDate)
            LocalDateTime startDateTime = reservation.getStartDate().atStartOfDay(); // Asumir que la fecha es a las 00:00

            // Calcular la duración en horas
            long hoursSinceCreation = java.time.Duration.between(startDateTime, now).toHours();

            // Solo cancelar si han pasado más de 24 horas y si no está confirmada o pagada
            if (hoursSinceCreation > 24 && !reservation.isPaid()) {
                reservation.setStatus(EReservationStatus.CANCELADO);
                reservationRepository.save(reservation); // Guardamos el estado actualizado
            }
        }
    }

}
