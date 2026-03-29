package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Обработка http запросов
@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private static final Logger log  = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService)
    {
        this.reservationService = reservationService;
    }
    @GetMapping("{id}")
    public ResponseEntity<Reservation> getReservationByID(@PathVariable("id") Long id)
    {
        log.info("Called getReservationByID="+id);
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservationByID(id));
        //return reservationService.getReservationByID(id);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations()
    {
        log.info("Called getAllReservations");
        return ResponseEntity.ok(reservationService.findAllReservation());
        //return reservationService.findAllReservation();
    }
//    @PutMapping("/{id}")
//    public Reservation updateUserInfobyID(@PathVariable("id") Long id)
//    {
//        return reservationService.updateUserInfobyID(id);
//    }
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation toCreate)
    {
        log.info("Called createReservation");
        return ResponseEntity.status(HttpStatus.CREATED).header(
                "test","123"
        ).body(reservationService.createReservation(toCreate));

        // return reservationService.createReservation(toCreate);
    }

}
