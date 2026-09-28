package com.FestPass.Service;

import com.FestPass.Dto.AttendeeRequest;
import com.FestPass.Repository.AttendeeRepository;
import org.springframework.stereotype.Service;
import com.FestPass.Models.Attendee;
import java.util.List;

@Service
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;

    public AttendeeService(AttendeeRepository attendeeRepository) {
        this.attendeeRepository = attendeeRepository;
    }

    public Attendee createAttendee(AttendeeRequest request) {

        Attendee attendee = new Attendee();

        attendee.setName(request.getName());
        attendee.setEmail(request.getEmail());
        attendee.setPhone(request.getPhone());

        return attendeeRepository.save(attendee);
    }

    public List<Attendee> getAllAttendees() {

        return attendeeRepository.findAll();
    }

    public Attendee getAttendeeById(Long id) {

        return attendeeRepository.findById(id)
                .orElse(null);
    }

    public Attendee updateAttendee(
            Long id,
            AttendeeRequest request) {

        Attendee attendee = attendeeRepository.findById(id)
                .orElse(null);

        if (attendee == null) {
            return null;
        }

        attendee.setName(request.getName());
        attendee.setEmail(request.getEmail());
        attendee.setPhone(request.getPhone());

        return attendeeRepository.save(attendee);
    }

    public void deleteAttendee(Long id) {

        attendeeRepository.deleteById(id);
    }
}