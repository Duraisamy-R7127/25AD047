package com.FestPass.Service;

import com.FestPass.Dto.FestEventRequest;
import com.FestPass.Models.FestEvent;
import com.FestPass.Repository.FestEventRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FestEventService {

    private final FestEventRepository festEventRepository;

    public FestEventService(FestEventRepository festEventRepository) {
        this.festEventRepository = festEventRepository;
    }

    public List<FestEvent> getAllEvents() {
        return festEventRepository.findAll();
    }

    public FestEvent getEventById(Long id) {
        return festEventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found with id: " + id));
    }

    public FestEvent createEvent(FestEventRequest request) {

        FestEvent event = new FestEvent();

        event.setName(request.getName());
        event.setCapacity(request.getCapacity());
        event.setTicketPrice(request.getTicketPrice());
        event.setEventDate(request.getEventDate());

        return festEventRepository.save(event);
    }

    public FestEvent updateEvent(Long id, FestEventRequest request) {

        FestEvent event = getEventById(id);

        event.setName(request.getName());
        event.setCapacity(request.getCapacity());
        event.setTicketPrice(request.getTicketPrice());
        event.setEventDate(request.getEventDate());

        return festEventRepository.save(event);
    }

    public void deleteEvent(Long id) {

        FestEvent event = getEventById(id);

        festEventRepository.delete(event);
    }
}