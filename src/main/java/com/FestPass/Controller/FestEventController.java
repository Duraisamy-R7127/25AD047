package com.FestPass.Controller;

import com.FestPass.Dto.FestEventRequest;
import com.FestPass.Models.FestEvent;
import com.FestPass.Service.FestEventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class FestEventController {

    private final FestEventService festEventService;

    public FestEventController(FestEventService festEventService) {
        this.festEventService = festEventService;
    }

    @PostMapping
    public ResponseEntity<FestEvent> createEvent(
            @Valid @RequestBody FestEventRequest request) {

        FestEvent event = festEventService.createEvent(request);

        return new ResponseEntity<>(
                event,
                HttpStatus.CREATED
        );
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

    @PutMapping("/{id}")
    public ResponseEntity<FestEvent> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody FestEventRequest request) {

        return ResponseEntity.ok(
                festEventService.updateEvent(id, request)
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(
            @PathVariable Long id) {

        festEventService.deleteEvent(id);

        return ResponseEntity.ok(
                "Event deleted successfully"
        );
    }
}