package com.FestPass.Controller;

import com.FestPass.Dto.AttendeeRequest;
import com.FestPass.Models.Attendee;
import com.FestPass.Service.AttendeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendees")
@CrossOrigin(origins = "*")
public class AttendeeController {

    private final AttendeeService attendeeService;

    public AttendeeController(
            AttendeeService attendeeService) {

        this.attendeeService =
                attendeeService;
    }

    @GetMapping
    public ResponseEntity<List<Attendee>>
    getAllAttendees() {

        return ResponseEntity.ok(
                attendeeService.getAllAttendees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendee>
    getAttendeeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                attendeeService.getAttendeeById(id));
    }

    @PostMapping
    public ResponseEntity<Attendee>
    createAttendee(
            @RequestBody AttendeeRequest request) {

        return ResponseEntity.ok(
                attendeeService.createAttendee(
                        request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attendee>
    updateAttendee(
            @PathVariable Long id,
            @RequestBody AttendeeRequest request) {

        return ResponseEntity.ok(
                attendeeService.updateAttendee(
                        id,
                        request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteAttendee(
            @PathVariable Long id) {

        attendeeService.deleteAttendee(id);

        return ResponseEntity.ok(
                "Attendee deleted successfully");
    }
}