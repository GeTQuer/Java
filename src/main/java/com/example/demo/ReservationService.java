package com.example.demo;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {
    private final Map<Long, Reservation> reservationMap;
    private final AtomicLong idCounter;
    public ReservationService()
    {
        reservationMap = new HashMap<>();
        idCounter = new AtomicLong();
    }
//    private static final Map<Long, Reservation> reservationMap = new HashMap<>(Map.of(
//            1L,new Reservation(
//                    1L,
//                    100L,
//                    40L,
//                    LocalDate.now(),
//                    LocalDate.now().plusDays(5),
//                    ReservationStatus.APPROVED
//            ),
//            2L,new Reservation(
//                    2L,
//                    101L,
//                    50L,
//                    LocalDate.now(),
//                    LocalDate.now().plusDays(3),
//                    ReservationStatus.CANCCELED
//            ),
//            3L, new Reservation(
//                    3L,
//                    104L,
//                    67L,
//                    LocalDate.now(),
//                    LocalDate.now().plusDays(4),
//                    ReservationStatus.PENDING
//            )
//    ));

    public Reservation getReservationByID(Long id)
    {
        if (!reservationMap.containsKey(id))
        {
            //return this.updateUserInfobyID(id);
            throw new NoSuchElementException("Not found id =" + id);
        }
        return reservationMap.get(id);
    }
    public List<Reservation> findAllReservation()
    {
        return reservationMap.values().stream().toList();
    }
//    public Reservation updateUserInfobyID(Long id)
//    {
//        reservationMap.put(
//                idCounter.incrementAndGet(), new Reservation(                id,
//                        67L,
//                        70L,
//                        LocalDate.now(),
//                        LocalDate.now().plusDays(3),
//                        ReservationStatus.CANCCELED)
//        );
//        return reservationMap.get(id);
//    }
    public Reservation createReservation(Reservation reservation)
    {
        if(reservation.id() != null)
            throw new IllegalArgumentException("It'd should be empty");
        if (reservation.status() != null)
        {
            throw new IllegalArgumentException("It'd should be empty");

        }
        var newReservation = new Reservation(
                idCounter.incrementAndGet(),
                reservation.userID(),
                reservation.placeID(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.PENDING
                );
        reservationMap.put(newReservation.id(), newReservation);
        return newReservation;
    }
    public Reservation updateReservation(Long id, Reservation reservationToUpdate)
    {
        if (!reservationMap.containsKey(id))
        {
            throw  new NoSuchElementException("Not found id");
        }
        var reservation =  reservationMap.get(id);
        if (reservation.status() != ReservationStatus.PENDING)
        {
            throw  new IllegalStateException("Cannot modify reservation");
        }
        var newReservation = new Reservation(
                id,
                reservationToUpdate.userID(),
                reservationToUpdate.placeID(),
                reservationToUpdate.startDate(),
                reservationToUpdate.endDate(),
                ReservationStatus.PENDING
        );
        reservationMap.put(id,newReservation);
        return newReservation;
    }
    public void deleteReservation(Long id)
    {

        if (!reservationMap.containsKey(id))
        {
            throw new NoSuchElementException("Not found reservation by id = " + id);
        }
        reservationMap.remove(id);
    }
    public Reservation approveReservation(Long id)
    {
        if (!reservationMap.containsKey(id))
        {
            throw new NoSuchElementException("Not found reservation by id = " + id);
        }
        var reservation = reservationMap.get(id);
        if (reservation.status()!= ReservationStatus.PENDING)
            throw new IllegalStateException("Cannot approve reservation: status = " + reservation.status());
        var isConflict = isReservationConflict(reservation);
        if (isConflict)
            throw new IllegalStateException("Cannot approve reservation: time conflict");
        var approvedReservation = new Reservation(
                reservation.id(),
                reservation.userID(),
                reservation.placeID(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.APPROVED
        );
        reservationMap.put(reservation.id(), approvedReservation);
        return approvedReservation;
    }
    private boolean isReservationConflict(Reservation reservation)
    {
        for (Reservation existingReservation: reservationMap.values())
        {
            if (reservation.id().equals(existingReservation.id()))
                continue;
            if (!reservation.placeID().equals(existingReservation.placeID()))
                continue;
            if (!existingReservation.status().equals(ReservationStatus.APPROVED))
                continue;
            if (reservation.startDate().isBefore(existingReservation.endDate()) &&
                    existingReservation.endDate().isBefore(reservation.endDate()))
                return true;
        }
        return false;
    }
}
