package com.FestPass.Controller;

import com.FestPass.Dto.BookingRequest;
import com.FestPass.Models.Booking;
import com.FestPass.Service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @Valid @RequestBody BookingRequest request) {

        Booking booking = bookingService.createBooking(request);

        return new ResponseEntity<>(
                booking,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {

        return ResponseEntity.ok(
                bookingService.getAllBookings()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.getBookingById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingRequest request) {

        return ResponseEntity.ok(
                bookingService.updateBooking(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(
            @PathVariable Long id) {

        bookingService.deleteBooking(id);

        return ResponseEntity.ok(
                "Booking deleted successfully"
        );
    }
}