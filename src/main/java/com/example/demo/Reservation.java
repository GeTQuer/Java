package com.example.demo;


import java.time.LocalDate;

//Отображает бронирование
public record Reservation(
        Long id,
        Long userID,
        Long placeID,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status
) {

}
