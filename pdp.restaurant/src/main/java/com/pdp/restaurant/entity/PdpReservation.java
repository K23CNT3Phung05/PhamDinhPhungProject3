package com.pdp.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pdp_reservations")
@Data
public class PdpReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;

    private String phone;

    @Column(name = "reservation_date")
    private String reservationDate;

    @Column(name = "reservation_time")
    private String reservationTime;

    @Column(name = "number_of_people")
    private int numberOfPeople;

    private String note;
    private String status; // PENDING, CONFIRMED, CANCELLED
}