package com.FestPass.Service;

import com.FestPass.Dto.BookingRequest;
import com.FestPass.Models.Attendee;
import com.FestPass.Models.Booking;
import com.FestPass.Models.FestEvent;
import com.FestPass.Models.Ticket;
import com.FestPass.Repository.AttendeeRepository;
import com.FestPass.Repository.BookingRepository;
import com.FestPass.Repository.FestEventRepository;
import com.FestPass.Repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AttendeeRepository attendeeRepository;
    private final FestEventRepository festEventRepository;
    private final TicketRepository ticketRepository;

    public BookingService(
            BookingRepository bookingRepository,
            AttendeeRepository attendeeRepository,
            FestEventRepository festEventRepository,
            TicketRepository ticketRepository) {

        this.bookingRepository = bookingRepository;
        this.attendeeRepository = attendeeRepository;
        this.festEventRepository = festEventRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }


    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));
    }

    @Transactional
    public Booking createBooking(BookingRequest request) {

        if (request == null) {
            throw new RuntimeException(
                    "Booking data is required");
        }

        if (request.getAttendeeId() == null) {
            throw new RuntimeException(
                    "Attendee ID is required");
        }

        if (request.getEventId() == null) {
            throw new RuntimeException(
                    "Event ID is required");
        }

        if (request.getNumberOfTickets() == null ||
                request.getNumberOfTickets() <= 0) {

            throw new RuntimeException(
                    "Number of tickets must be greater than 0");
        }

        Attendee attendee =
                attendeeRepository.findById(
                        request.getAttendeeId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Attendee not found"));

        FestEvent event =
                festEventRepository.findById(
                        request.getEventId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Event not found"));

        long soldTickets =
                ticketRepository.countByBooking_Event_Id(
                        event.getId());

        long requested =
                request.getNumberOfTickets();

        if (soldTickets + requested > event.getCapacity()) {

            throw new RuntimeException(
                    "EVENT SOLD OUT: Capacity "
                            + event.getCapacity()
                            + " / "
                            + soldTickets);
        }

        // -------------------------------------------------
        // Create Booking
        // -------------------------------------------------

        Booking booking = new Booking();

        booking.setAttendee(attendee);
        booking.setEvent(event);
        booking.setNumberOfTickets(
                request.getNumberOfTickets());

        Booking savedBooking =
                bookingRepository.save(booking);

        // -------------------------------------------------
        // Create Tickets
        // -------------------------------------------------

        List<Ticket> tickets = new ArrayList<>();

        for (int i = 0;
             i < request.getNumberOfTickets();
             i++) {

            Ticket ticket = new Ticket();

            ticket.setBooking(savedBooking);

            ticket.setTicketType("REGULAR");

            // Double -> BigDecimal
            ticket.setPrice(
                    BigDecimal.valueOf(
                            event.getTicketPrice()
                    )
            );

            // Temporary unique ticket number
            ticket.setTicketNumber(
                    "TEMP-" + UUID.randomUUID());

            // Unique QR code
            ticket.setQrCode(
                    "FESTPASS-" + UUID.randomUUID());

            // Initial check-in status
            ticket.setCheckedIn(false);
            ticket.setCheckedInAt(null);

            // Save first to generate ID
            Ticket savedTicket =
                    ticketRepository.save(ticket);

            // -------------------------------------------------
            // Final Ticket Number
            // -------------------------------------------------

            int year =
                    event.getEventDate() != null
                            ? event.getEventDate().getYear()
                            : LocalDateTime.now().getYear();

            String finalTicketNumber =
                    String.format(
                            "FP-%d-%06d",
                            year,
                            savedTicket.getId());

            savedTicket.setTicketNumber(
                    finalTicketNumber);

            Ticket finalTicket =
                    ticketRepository.save(savedTicket);

            tickets.add(finalTicket);
        }

        // -------------------------------------------------
        // Add Tickets To Booking
        // -------------------------------------------------

        savedBooking.setTickets(tickets);

        return savedBooking;
    }

    // =====================================================
    // DELETE BOOKING
    // =====================================================

    public void deleteBooking(Long id) {

        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException(
                    "Booking not found");
        }

        bookingRepository.deleteById(id);
    }
}