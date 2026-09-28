package com.FestPass.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // TICKET NUMBER
    // =====================================================

    @Column(nullable = false, unique = true)
    private String ticketNumber;

    // =====================================================
    // TICKET TYPE
    // =====================================================

    @Column(nullable = false)
    private String ticketType;

    // =====================================================
    // PRICE
    // =====================================================

    @Column(nullable = false)
    private BigDecimal price;

    // =====================================================
    // QR CODE
    // =====================================================

    @Column(nullable = false, unique = true)
    private String qrCode;

    // =====================================================
    // CHECK IN
    // =====================================================

    @Column(nullable = false)
    private boolean checkedIn = false;

    private LocalDateTime checkedInAt;

    // =====================================================
    // CHECK OUT
    // =====================================================

    @Column(nullable = false)
    private boolean checkedOut = false;

    private LocalDateTime checkedOutAt;

    // =====================================================
    // BOOKING
    // =====================================================

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Ticket() {
    }

    // =====================================================
    // ID
    // =====================================================

    public Long getId() {
        return id;
    }

    // =====================================================
    // TICKET NUMBER
    // =====================================================

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    // =====================================================
    // TICKET TYPE
    // =====================================================

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    // =====================================================
    // PRICE
    // =====================================================

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // =====================================================
    // QR CODE
    // =====================================================

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    // =====================================================
    // CHECK IN
    // =====================================================

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    // =====================================================
    // CHECK OUT
    // =====================================================

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public LocalDateTime getCheckedOutAt() {
        return checkedOutAt;
    }

    public void setCheckedOutAt(LocalDateTime checkedOutAt) {
        this.checkedOutAt = checkedOutAt;
    }

    // =====================================================
    // BOOKING
    // =====================================================

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}