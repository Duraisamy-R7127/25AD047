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

    @GetMapping
    public List<Ticket> getAllTickets() {

        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(
            @PathVariable Long id) {

        return ticketService.getTicketById(id);
    }

    @GetMapping("/qr/{qrCode}")
    public Ticket getTicketByQrCode(
            @PathVariable String qrCode) {

        return ticketService.getTicketByQrCode(qrCode);
    }


    @PutMapping("/check-in/{qrCode}")
    public Ticket checkIn(
            @PathVariable String qrCode) {

        return ticketService.checkInTicket(qrCode);
    }

    @PutMapping("/check-out/{qrCode}")
    public Ticket checkOut(
            @PathVariable String qrCode) {

        return ticketService.checkOutTicket(qrCode);
    }
    @DeleteMapping("/{id}")
    public String deleteTicket(
            @PathVariable Long id) {

        ticketService.deleteTicket(id);

        return "Ticket deleted successfully";
    }
}