package com.FestPass.Controller;

import com.FestPass.Models.Ticket;
import com.FestPass.Service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(
            TicketService ticketService) {

        this.ticketService = ticketService;
    }

    // =====================================================
    // GET ALL TICKETS
    // GET /tickets
    // =====================================================

    @GetMapping
    public List<Ticket> getAllTickets() {

        return ticketService.getAllTickets();
    }

    // =====================================================
    // GET TICKET BY ID
    // GET /tickets/1
    // =====================================================

    @GetMapping("/{id}")
    public Ticket getTicketById(
            @PathVariable Long id) {

        return ticketService.getTicketById(id);
    }

    // =====================================================
    // GET TICKET BY QR
    // GET /tickets/qr/xxxxx
    // =====================================================

    @GetMapping("/qr/{qrCode}")
    public Ticket getTicketByQrCode(
            @PathVariable String qrCode) {

        return ticketService.getTicketByQrCode(qrCode);
    }

    // =====================================================
    // CHECK IN
    // PUT /tickets/check-in/xxxxx
    // =====================================================

    @PutMapping("/check-in/{qrCode}")
    public Ticket checkIn(
            @PathVariable String qrCode) {

        return ticketService.checkInTicket(qrCode);
    }

    // =====================================================
    // CHECK OUT
    // PUT /tickets/check-out/xxxxx
    // =====================================================

    @PutMapping("/check-out/{qrCode}")
    public Ticket checkOut(
            @PathVariable String qrCode) {

        return ticketService.checkOutTicket(qrCode);
    }

    // =====================================================
    // DELETE
    // DELETE /tickets/1
    // =====================================================

    @DeleteMapping("/{id}")
    public String deleteTicket(
            @PathVariable Long id) {

        ticketService.deleteTicket(id);

        return "Ticket deleted successfully";
    }
}