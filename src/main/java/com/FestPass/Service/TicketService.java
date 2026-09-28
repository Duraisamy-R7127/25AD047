package com.FestPass.Service;

import com.FestPass.Dto.TicketRequest;
import com.FestPass.Models.Booking;
import com.FestPass.Models.Ticket;
import com.FestPass.Repository.BookingRepository;
import com.FestPass.Repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;

    public TicketService(
            TicketRepository ticketRepository,
            BookingRepository bookingRepository) {

        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
    }

    // =====================================================
    // GET ALL TICKETS
    // =====================================================

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // =====================================================
    // GET TICKET BY ID
    // =====================================================

    public Ticket getTicketById(Long id) {

        return ticketRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));
    }

    // =====================================================
    // GET TICKET BY QR CODE
    // =====================================================

    public Ticket getTicketByQrCode(String qrCode) {

        return ticketRepository.findByQrCode(qrCode)
                .orElseThrow(() ->
                        new RuntimeException("INVALID TICKET"));
    }

    // =====================================================
    // CREATE TICKET
    // =====================================================

    public Ticket createTicket(TicketRequest request) {

        if (request == null) {
            throw new RuntimeException(
                    "Ticket request is required");
        }

        if (request.getBookingId() == null) {
            throw new RuntimeException(
                    "Booking ID is required");
        }

        Booking booking = bookingRepository
                .findById(request.getBookingId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"));

        Ticket ticket = new Ticket();

        // -------------------------------------------------
        // UNIQUE TICKET NUMBER
        // -------------------------------------------------

        String ticketNumber;

        do {

            ticketNumber =
                    "FP-" +
                            System.currentTimeMillis() +
                            "-" +
                            UUID.randomUUID()
                                    .toString()
                                    .substring(0, 4)
                                    .toUpperCase();

        } while (
                ticketRepository
                        .findByTicketNumber(ticketNumber)
                        .isPresent()
        );

        ticket.setTicketNumber(ticketNumber);

        // -------------------------------------------------
        // TICKET TYPE
        // -------------------------------------------------

        if (request.getTicketType() != null &&
                !request.getTicketType().isBlank()) {

            ticket.setTicketType(
                    request.getTicketType());

        } else {

            ticket.setTicketType("GENERAL");
        }

        // -------------------------------------------------
        // PRICE
        // Get price from Booking -> Event
        // -------------------------------------------------

        Double eventPrice =
                booking.getEvent().getTicketPrice();

        if (eventPrice != null) {

            ticket.setPrice(
                    BigDecimal.valueOf(eventPrice));

        } else {

            ticket.setPrice(BigDecimal.ZERO);
        }

        // -------------------------------------------------
        // UNIQUE QR CODE
        // -------------------------------------------------

        String qrCode;

        do {

            qrCode =
                    "FESTPASS-" +
                            UUID.randomUUID()
                                    .toString()
                                    .replace("-", "");

        } while (
                ticketRepository
                        .findByQrCode(qrCode)
                        .isPresent()
        );

        ticket.setQrCode(qrCode);

        // -------------------------------------------------
        // CHECK IN
        // -------------------------------------------------

        ticket.setCheckedIn(false);
        ticket.setCheckedInAt(null);

        // -------------------------------------------------
        // CHECK OUT
        // -------------------------------------------------

        ticket.setCheckedOut(false);
        ticket.setCheckedOutAt(null);

        // -------------------------------------------------
        // BOOKING
        // -------------------------------------------------

        ticket.setBooking(booking);

        // -------------------------------------------------
        // SAVE
        // -------------------------------------------------

        return ticketRepository.save(ticket);
    }

    // =====================================================
    // CHECK IN
    // =====================================================

    public Ticket checkIn(String qrCode) {

        Ticket ticket = ticketRepository
                .findByQrCode(qrCode)
                .orElseThrow(() ->
                        new RuntimeException(
                                "INVALID TICKET"));

        // Already checked in
        if (ticket.isCheckedIn()) {

            throw new RuntimeException(
                    "Ticket Already Checked In");
        }

        // Check in
        ticket.setCheckedIn(true);

        ticket.setCheckedInAt(
                LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    // =====================================================
    // CHECK OUT
    // =====================================================

    public Ticket checkOut(String qrCode) {

        Ticket ticket = ticketRepository
                .findByQrCode(qrCode)
                .orElseThrow(() ->
                        new RuntimeException(
                                "INVALID TICKET"));

        // Must check in first
        if (!ticket.isCheckedIn()) {

            throw new RuntimeException(
                    "Ticket is not checked in");
        }

        // Already checked out
        if (ticket.isCheckedOut()) {

            throw new RuntimeException(
                    "Ticket Already Checked Out");
        }

        // Check out
        ticket.setCheckedOut(true);

        ticket.setCheckedOutAt(
                LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    // =====================================================
    // CHECK IN - CONTROLLER METHOD
    // =====================================================

    public Ticket checkInTicket(String qrCode) {

        return checkIn(qrCode);
    }

    // =====================================================
    // CHECK OUT - CONTROLLER METHOD
    // =====================================================

    public Ticket checkOutTicket(String qrCode) {

        return checkOut(qrCode);
    }

    // =====================================================
    // DELETE TICKET
    // =====================================================

    public void deleteTicket(Long id) {

        Ticket ticket = ticketRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ticket not found"));

        ticketRepository.delete(ticket);
    }
}