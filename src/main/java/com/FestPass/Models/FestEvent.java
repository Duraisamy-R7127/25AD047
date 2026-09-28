package com.FestPass.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fest_event")
public class FestEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Double ticketPrice;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    public FestEvent() {
    }

    // =========================
    // ID
    // =========================

    public Long getId() {
        return id;
    }

    // =========================
    // NAME
    // =========================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // =========================
    // VENUE
    // =========================

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    // =========================
    // CAPACITY
    // =========================

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    // =========================
    // TICKET PRICE
    // =========================

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    // =========================
    // EVENT DATE
    // =========================

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }
}