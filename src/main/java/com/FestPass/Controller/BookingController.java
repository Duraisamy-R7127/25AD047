package com.FestPass.Controller;

import com.FestPass.Dto.BookingRequest;
import com.FestPass.Models.Booking;
import com.FestPass.Service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(
            BookingService bookingService) {

        this.bookingService =
                bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Booking>>
    getAllBookings() {

        return ResponseEntity.ok(
                bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking>
    getBookingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.getBookingById(id));
    }

    @PostMapping
    public ResponseEntity<Booking>
    createBooking(
            @RequestBody BookingRequest request) {

        return ResponseEntity.ok(
                bookingService.createBooking(
                        request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteBooking(
            @PathVariable Long id) {

        bookingService.deleteBooking(id);

        return ResponseEntity.ok(
                "Booking deleted successfully");
    }
}