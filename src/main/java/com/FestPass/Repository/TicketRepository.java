package com.FestPass.Repository;

import com.FestPass.Models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketNumber(String ticketNumber);

    Optional<Ticket> findByQrCode(String qrCode);

    long countByCheckedInTrue();

    long countByBooking_Event_Id(Long eventId);

    long countByBooking_Event_IdAndCheckedInTrue(Long eventId);
}