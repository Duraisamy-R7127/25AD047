package com.FestPass.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attendee_id", nullable = false)
    private Attendee attendee;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private FestEvent event;

    @Column(nullable = false)
    private Integer numberOfTickets;

    private LocalDateTime bookingDate;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Ticket> tickets = new ArrayList<>();

    public Booking() {
    }

    @PrePersist
    protected void onCreate() {
        if (bookingDate == null) {
            bookingDate = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public FestEvent getEvent() {
        return event;
    }

    public void setEvent(FestEvent event) {
        this.event = event;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}