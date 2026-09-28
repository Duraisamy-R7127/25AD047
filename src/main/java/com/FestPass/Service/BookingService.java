package com.FestPass.Service;

import com.FestPass.Dto.BookingRequest;
import com.FestPass.Models.Attendee;
import com.FestPass.Models.Booking;
import com.FestPass.Models.FestEvent;
import com.FestPass.Repository.AttendeeRepository;
import com.FestPass.Repository.BookingRepository;
import com.FestPass.Repository.FestEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AttendeeRepository attendeeRepository;
    private final FestEventRepository festEventRepository;

    public BookingService(
            BookingRepository bookingRepository,
            AttendeeRepository attendeeRepository,
            FestEventRepository festEventRepository) {

        this.bookingRepository = bookingRepository;
        this.attendeeRepository = attendeeRepository;
        this.festEventRepository = festEventRepository;
    }

    public Booking createBooking(BookingRequest request) {

        Attendee attendee = attendeeRepository
                .findById(request.getAttendeeId())
                .orElse(null);

        FestEvent event = festEventRepository
                .findById(request.getEventId())
                .orElse(null);

        if (attendee == null || event == null) {
            return null;
        }

        Booking booking = new Booking();

        booking.setAttendee(attendee);
        booking.setEvent(event);
        booking.setNumberOfTickets(
                request.getNumberOfTickets()
        );

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {

        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElse(null);
    }

    public Booking updateBooking(
            Long id,
            BookingRequest request) {

        Booking booking = bookingRepository
                .findById(id)
                .orElse(null);

        if (booking == null) {
            return null;
        }

        Attendee attendee = attendeeRepository
                .findById(request.getAttendeeId())
                .orElse(null);

        FestEvent event = festEventRepository
                .findById(request.getEventId())
                .orElse(null);

        if (attendee == null || event == null) {
            return null;
        }

        booking.setAttendee(attendee);
        booking.setEvent(event);
        booking.setNumberOfTickets(
                request.getNumberOfTickets()
        );

        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {

        bookingRepository.deleteById(id);
    }
}