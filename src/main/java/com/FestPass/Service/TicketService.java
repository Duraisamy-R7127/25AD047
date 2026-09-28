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


    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {

        return ticketRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));
    }

    public Ticket getTicketByQrCode(String qrCode) {

        return ticketRepository.findByQrCode(qrCode)
                .orElseThrow(() ->
                        new RuntimeException("INVALID TICKET"));
    }

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

        if (request.getTicketType() != null &&
                !request.getTicketType().isBlank()) {

            ticket.setTicketType(
                    request.getTicketType());

        } else {

            ticket.setTicketType("GENERAL");
        }
        Double eventPrice =
                booking.getEvent().getTicketPrice();

        if (eventPrice != null) {

            ticket.setPrice(
                    BigDecimal.valueOf(eventPrice));

        } else {

            ticket.setPrice(BigDecimal.ZERO);
        }

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


        ticket.setCheckedIn(false);
        ticket.setCheckedInAt(null);

        ticket.setCheckedOut(false);
        ticket.setCheckedOutAt(null);


        ticket.setBooking(booking);

        return ticketRepository.save(ticket);
    }

    public Ticket checkIn(String qrCode) {

        Ticket ticket = ticketRepository
                .findByQrCode(qrCode)
                .orElseThrow(() ->
                        new RuntimeException(
                                "INVALID TICKET"));
        if (ticket.isCheckedIn()) {

            throw new RuntimeException(
                    "Ticket Already Checked In");
        }

        ticket.setCheckedIn(true);

        ticket.setCheckedInAt(
                LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public Ticket checkOut(String qrCode) {

        Ticket ticket = ticketRepository
                .findByQrCode(qrCode)
                .orElseThrow(() ->
                        new RuntimeException(
                                "INVALID TICKET"));

        if (!ticket.isCheckedIn()) {

            throw new RuntimeException(
                    "Ticket is not checked in");
        }
        if (ticket.isCheckedOut()) {

            throw new RuntimeException(
                    "Ticket Already Checked Out");
        }
        ticket.setCheckedOut(true);

        ticket.setCheckedOutAt(
                LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public Ticket checkInTicket(String qrCode) {

        return checkIn(qrCode);
    }

    public Ticket checkOutTicket(String qrCode) {

        return checkOut(qrCode);
    }

    public void deleteTicket(Long id) {

        Ticket ticket = ticketRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ticket not found"));

        ticketRepository.delete(ticket);
    }
}