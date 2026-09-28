package com.FestPass.Repository;

import com.FestPass.Models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByQrCode(String qrCode);

    Optional<Ticket> findByTicketNumber(String ticketNumber);
}