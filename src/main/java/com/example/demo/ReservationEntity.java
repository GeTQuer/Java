package com.example.demo;
import jakarta.persistence.*;

import java.time.LocalDate;

@Table(name = "reservations") // Имя новой таблицы
@Entity
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // БД сама генерирует id сущностям

    @Column(name = "id")
    private  Long id;

    @Column(name = "user_id")
    private Long userID;

    @Column(name = "place_id")
    private Long placeID;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    public ReservationEntity(){

    }
    public ReservationEntity(
            Long id,
            Long userID,
            Long placeID,
            LocalDate startDate,
            LocalDate endDate,
            ReservationStatus status) {
        this.id = id;
        this.userID = userID;
        this.placeID = placeID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getPlaceID() {
        return placeID;
    }

    public void setPlaceID(Long placeID) {
        this.placeID = placeID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return this.id;
    }
}
