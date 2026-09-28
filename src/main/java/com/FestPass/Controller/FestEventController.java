package com.FestPass.Controller;

import com.FestPass.Dto.FestEventRequest;
import com.FestPass.Models.FestEvent;
import com.FestPass.Service.FestEventService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@CrossOrigin
public class FestEventController {

    private final FestEventService festEventService;

    public FestEventController(FestEventService festEventService) {
        this.festEventService = festEventService;
    }

    @GetMapping
    public ResponseEntity<List<FestEvent>> getAllEvents() {

        return ResponseEntity.ok(
                festEventService.getAllEvents()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FestEvent> getEventById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                festEventService.getEventById(id)
        );
    }

    @PostMapping
    public ResponseEntity<FestEvent> createEvent(
            @RequestBody FestEventRequest request) {

        return ResponseEntity.ok(
                festEventService.createEvent(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<FestEvent> updateEvent(
            @PathVariable Long id,
            @RequestBody FestEventRequest request) {

        return ResponseEntity.ok(
                festEventService.updateEvent(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(
            @PathVariable Long id) {

        festEventService.deleteEvent(id);

        return ResponseEntity.ok("Event deleted successfully");
    }
}