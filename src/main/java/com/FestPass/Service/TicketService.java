package com.FestPass.Service;

import com.FestPass.Dto.TicketRequest;
import com.FestPass.Models.Booking;
import com.FestPass.Models.Ticket;
import com.FestPass.Repository.BookingRepository;
import com.FestPass.Repository.TicketRepository;
import org.springframework.stereotype.Service;

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

    // CREATE TICKET
    public Ticket createTicket(Long bookingId, String ticketType) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found with ID: " + bookingId)
                );

        Ticket ticket = new Ticket();

        ticket.setTicketNumber(
                "TKT-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );

        ticket.setQrCode(
                UUID.randomUUID().toString()
        );

        ticket.setTicketType(ticketType);

        /*
         * Price booking-லிருந்து automatically எடுத்துக்கொள்ளாமல்
         * initially 0.0 வைக்கிறோம்.
         *
         * POST request-ல் price கொடுத்தால் கீழே controller/service
         * alternate method use பண்ணலாம்.
         */
        ticket.setPrice(0.0);

        ticket.setCheckedIn(false);

        ticket.setBooking(booking);

        return ticketRepository.save(ticket);
    }

    // CREATE TICKET WITH PRICE
    public Ticket createTicket(
            Long bookingId,
            String ticketType,
            Double price) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found with ID: " + bookingId)
                );

        Ticket ticket = new Ticket();

        ticket.setTicketNumber(
                "TKT-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );

        ticket.setQrCode(
                UUID.randomUUID().toString()
        );

        ticket.setTicketType(ticketType);
        ticket.setPrice(price);
        ticket.setCheckedIn(false);
        ticket.setBooking(booking);

        return ticketRepository.save(ticket);
    }

    // GET ALL
    public List<Ticket> getAllTickets() {

        return ticketRepository.findAll();
    }

    // GET BY ID
    public Ticket getTicketById(Long id) {

        return ticketRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found with ID: " + id)
                );
    }

    // UPDATE
    public Ticket updateTicket(
            Long id,
            TicketRequest request) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found with ID: " + id)
                );

        Booking booking = bookingRepository.findById(
                request.getBookingId()
        ).orElseThrow(() ->
                new RuntimeException(
                        "Booking not found with ID: "
                                + request.getBookingId()
                )
        );

        ticket.setTicketType(request.getTicketType());
        ticket.setPrice(request.getPrice());
        ticket.setBooking(booking);

        return ticketRepository.save(ticket);
    }

    // DELETE
    public void deleteTicket(Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found with ID: " + id)
                );

        ticketRepository.delete(ticket);
    }

    // GET BY QR
    public Ticket getTicketByQrCode(String qrCode) {

        return ticketRepository.findByQrCode(qrCode)
                .orElseThrow(() ->
                        new RuntimeException("Invalid QR code")
                );
    }

    // CHECK IN
    public Ticket checkIn(String qrCode) {

        Ticket ticket = ticketRepository.findByQrCode(qrCode)
                .orElseThrow(() ->
                        new RuntimeException("Invalid QR code")
                );

        if (ticket.isCheckedIn()) {
            throw new RuntimeException(
                    "Ticket already checked in"
            );
        }

        ticket.setCheckedIn(true);

        return ticketRepository.save(ticket);
    }
}