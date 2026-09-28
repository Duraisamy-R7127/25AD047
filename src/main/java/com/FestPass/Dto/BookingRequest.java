package com.FestPass.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequest {

    @NotNull(message = "Attendee ID is required")
    private Long attendeeId;

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Number of tickets is required")
    @Positive(message = "Number of tickets must be greater than zero")
    private Integer numberOfTickets;

    public BookingRequest() {
    }

    public Long getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(Long attendeeId) {
        this.attendeeId = attendeeId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
}