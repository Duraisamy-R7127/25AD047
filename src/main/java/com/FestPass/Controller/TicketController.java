package com.FestPass.Controller;

import com.FestPass.Dto.TicketRequest;
import com.FestPass.Models.Ticket;
import com.FestPass.Service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(
            @Valid @RequestBody TicketRequest request) {

        Ticket ticket = ticketService.createTicket(
                request.getBookingId(),
                request.getTicketType(),
                request.getPrice()
        );

        return new ResponseEntity<>(
                ticket,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {

        return ResponseEntity.ok(
                ticketService.getAllTickets()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ticketService.getTicketById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketRequest request) {

        return ResponseEntity.ok(
                ticketService.updateTicket(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(
            @PathVariable Long id) {

        ticketService.deleteTicket(id);

        return ResponseEntity.ok(
                "Ticket deleted successfully"
        );
    }

    @GetMapping("/qr/{qrCode}")
    public ResponseEntity<Ticket> getTicketByQrCode(
            @PathVariable String qrCode) {

        return ResponseEntity.ok(
                ticketService.getTicketByQrCode(qrCode)
        );
    }

    @PutMapping("/checkin/{qrCode}")
    public ResponseEntity<Ticket> checkIn(
            @PathVariable String qrCode) {

        return ResponseEntity.ok(
                ticketService.checkIn(qrCode)
        );
    }
}