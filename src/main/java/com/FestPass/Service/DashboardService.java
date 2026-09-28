package com.FestPass.Service;
import com.FestPass.Models.FestEvent;
import com.FestPass.Dto.DashboardResponse;
import com.FestPass.Repository.FestEventRepository;
import com.FestPass.Repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final FestEventRepository festEventRepository;
    private final TicketRepository ticketRepository;

    public DashboardService(
            FestEventRepository festEventRepository,
            TicketRepository ticketRepository) {

        this.festEventRepository =
                festEventRepository;

        this.ticketRepository =
                ticketRepository;
    }

    public DashboardResponse getDashboard() {

        long totalEvents =
                festEventRepository.count();

        long ticketsSold =
                ticketRepository.count();

        long checkedIn =
                ticketRepository.countByCheckedInTrue();

        List<DashboardResponse.EventOverview>
                eventOverviewList = new ArrayList<>();

        List<FestEvent> events =
                festEventRepository.findAll();

        for (FestEvent event : events) {

            long sold =
                    ticketRepository
                            .countByBooking_Event_Id(
                                    event.getId());

            long checked =
                    ticketRepository
                            .countByBooking_Event_IdAndCheckedInTrue(
                                    event.getId());

            DashboardResponse.EventOverview
                    overview =
                    new DashboardResponse.EventOverview(
                            event.getId(),
                            event.getName(),
                            event.getVenue(),
                            event.getCapacity(),
                            event.getTicketPrice(),
                            sold,
                            checked
                    );

            eventOverviewList.add(overview);
        }

        return new DashboardResponse(
                totalEvents,
                ticketsSold,
                checkedIn,
                eventOverviewList
        );
    }
}