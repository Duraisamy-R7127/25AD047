package com.FestPass.Dto;

import java.util.ArrayList;
import java.util.List;

public class DashboardResponse {

    private long totalEvents;
    private long ticketsSold;
    private long checkedIn;
    private List<EventOverview> events = new ArrayList<>();

    public DashboardResponse() {
    }

    public DashboardResponse(
            long totalEvents,
            long ticketsSold,
            long checkedIn,
            List<EventOverview> events) {

        this.totalEvents = totalEvents;
        this.ticketsSold = ticketsSold;
        this.checkedIn = checkedIn;
        this.events = events;
    }

    public long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public long getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(long ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public long getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(long checkedIn) {
        this.checkedIn = checkedIn;
    }

    public List<EventOverview> getEvents() {
        return events;
    }

    public void setEvents(List<EventOverview> events) {
        this.events = events;
    }

    public static class EventOverview {

        private Long eventId;
        private String eventName;
        private String venue;
        private Integer capacity;
        private Double ticketPrice;
        private long ticketsSold;
        private long checkedIn;
        private long remaining;
        private long notCheckedIn;
        private double attendancePercentage;

        public EventOverview() {
        }

        public EventOverview(
                Long eventId,
                String eventName,
                String venue,
                Integer capacity,
                Double ticketPrice,
                long ticketsSold,
                long checkedIn) {

            this.eventId = eventId;
            this.eventName = eventName;
            this.venue = venue;
            this.capacity = capacity;
            this.ticketPrice = ticketPrice;
            this.ticketsSold = ticketsSold;
            this.checkedIn = checkedIn;
            this.remaining = Math.max(0, capacity - ticketsSold);
            this.notCheckedIn = Math.max(0, ticketsSold - checkedIn);

            if (capacity != null && capacity > 0) {
                this.attendancePercentage =
                        (checkedIn * 100.0) / capacity;
            } else {
                this.attendancePercentage = 0.0;
            }
        }

        public Long getEventId() {
            return eventId;
        }

        public String getEventName() {
            return eventName;
        }

        public String getVenue() {
            return venue;
        }

        public Integer getCapacity() {
            return capacity;
        }

        public Double getTicketPrice() {
            return ticketPrice;
        }

        public long getTicketsSold() {
            return ticketsSold;
        }

        public long getCheckedIn() {
            return checkedIn;
        }

        public long getRemaining() {
            return remaining;
        }

        public long getNotCheckedIn() {
            return notCheckedIn;
        }

        public double getAttendancePercentage() {
            return attendancePercentage;
        }
    }
}