package com.example.demo;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.proxy.EntityNotFoundDelegate;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    public ReservationService(ReservationRepository repository)
    {
        this.repository = repository;

    }
    public Reservation getReservationByID(Long id)
    {
        ReservationEntity reservationEntity =  repository.findById(id).orElseThrow(()-> new EntityNotFoundException(
                "Not found reservation by id = " + id
        ));
        return toDomainReservation(reservationEntity);

    }
    public List<Reservation> findAllReservation()
    {
        List<ReservationEntity> allEntities = repository.findAll();
        return  allEntities.stream().map(
                this::toDomainReservation).toList();}

    public Reservation createReservation(Reservation reservationToCreate)
    {
        if(reservationToCreate.id() != null)
            throw new IllegalArgumentException("It'd should be empty");
        if (reservationToCreate.status() != null)
        {
            throw new IllegalArgumentException("It'd should be empty");

        }
        var entityToSave = new ReservationEntity(
                null,
                reservationToCreate.userID(),
                reservationToCreate.placeID(),
                reservationToCreate.startDate(),
                reservationToCreate.endDate(),
                ReservationStatus.PENDING
                );
        var savedEntity = repository.save(entityToSave);
        return toDomainReservation(savedEntity);
    }
    public Reservation updateReservation(Long id, Reservation reservationToUpdate)
    {

        var reservationEntity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Not found reservation by id = " + id));
        if (reservationEntity.getStatus() != ReservationStatus.PENDING)
        {
            throw  new IllegalStateException("Cannot modify reservation status = " + reservationEntity.getStatus());
        }
        var entityToSave = new ReservationEntity(
                reservationEntity.getId(),
                reservationToUpdate.userID(),
                reservationToUpdate.placeID(),
                reservationToUpdate.startDate(),
                reservationToUpdate.endDate(),
                ReservationStatus.PENDING
        );
        var savedEntity = repository.save(entityToSave);
        return toDomainReservation(savedEntity);
    }
    public void deleteReservation(Long id)
    {

        if (!repository.existsById(id))
        {
            throw new EntityNotFoundException("Not found reservation by id = " + id);
        }
        repository.deleteById(id);
    }
    public Reservation approveReservation(Long id)
    {
        var reservationEntity = repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Not found reservation by id = " + id));

        if (reservationEntity.getStatus()!= ReservationStatus.PENDING)
            throw new IllegalStateException("Cannot approve reservation: status = " + reservationEntity.getStatus());
        var isConflict = isReservationConflict(reservationEntity);
        if (isConflict)
            throw new IllegalStateException("Cannot approve reservation: time conflict");
        var approvedReservation = new Reservation(
                reservationEntity.getId(),
                reservationEntity.getUserID(),
                reservationEntity.getPlaceID(),
                reservationEntity.getStartDate(),
                reservationEntity.getEndDate(),
                ReservationStatus.APPROVED
        );
        reservationEntity.setStatus(ReservationStatus.APPROVED);
        repository.save(reservationEntity);
        return toDomainReservation(reservationEntity);
    }
    private boolean isReservationConflict(ReservationEntity reservation)
    {
        var allReservations = repository.findAll();
        for (ReservationEntity existingReservation: allReservations)
        {
            if (reservation.getId().equals(existingReservation.getId()))
                continue;
            if (!reservation.getPlaceID().equals(existingReservation.getPlaceID()))
                continue;
            if (!existingReservation.getStatus().equals(ReservationStatus.APPROVED))
                continue;
            if (reservation.getStartDate().isBefore(existingReservation.getEndDate()) &&
                    existingReservation.getEndDate().isBefore(reservation.getEndDate()))
                return true;
        }
        return false;
    }

    private Reservation toDomainReservation(ReservationEntity reservationEntity)
    {
        return new Reservation(
                reservationEntity.getId(),
                reservationEntity.getUserID(),
                reservationEntity.getPlaceID(),
                reservationEntity.getStartDate(),
                reservationEntity.getEndDate(),
                reservationEntity.getStatus()
        );
    }
}
