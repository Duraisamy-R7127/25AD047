package com.FestPass.Service;

import com.FestPass.Dto.AttendeeRequest;
import com.FestPass.Models.Attendee;
import com.FestPass.Repository.AttendeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;

    public AttendeeService(
            AttendeeRepository attendeeRepository) {

        this.attendeeRepository = attendeeRepository;
    }

    public List<Attendee> getAllAttendees() {
        return attendeeRepository.findAll();
    }

    public Attendee getAttendeeById(Long id) {

        return attendeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Attendee not found"));
    }

    public Attendee createAttendee(
            AttendeeRequest request) {

        if (request == null) {
            throw new RuntimeException(
                    "Attendee data is required");
        }

        if (request.getName() == null ||
                request.getName().trim().isEmpty()) {

            throw new RuntimeException(
                    "Name is required");
        }

        if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty()) {

            throw new RuntimeException(
                    "Email is required");
        }

        Attendee attendee = new Attendee();

        attendee.setName(request.getName());
        attendee.setEmail(request.getEmail());
        attendee.setPhone(request.getPhone());

        return attendeeRepository.save(attendee);
    }

    public Attendee updateAttendee(
            Long id,
            AttendeeRequest request) {

        Attendee attendee = getAttendeeById(id);

        attendee.setName(request.getName());
        attendee.setEmail(request.getEmail());
        attendee.setPhone(request.getPhone());

        return attendeeRepository.save(attendee);
    }

    public void deleteAttendee(Long id) {

        if (!attendeeRepository.existsById(id)) {
            throw new RuntimeException(
                    "Attendee not found");
        }

        attendeeRepository.deleteById(id);
    }
}