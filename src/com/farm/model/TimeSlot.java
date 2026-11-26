
package com.farm.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot {

    private String slotId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate slotDate;
    private boolean available;

    // Constructor for a slot scheduled today
    public TimeSlot(String slotId, LocalTime startTime,
                    LocalTime endTime) {

        this(slotId, startTime, endTime, LocalDate.now());
    }

    // Constructor with a specific date
    public TimeSlot(String slotId, LocalTime startTime,
                    LocalTime endTime, LocalDate slotDate) {

        if (slotId == null || slotId.trim().isEmpty())
            throw new IllegalArgumentException("Slot ID cannot be empty.");

        if (startTime == null || endTime == null)
            throw new IllegalArgumentException("Time cannot be null.");

        if (!startTime.isBefore(endTime))
            throw new IllegalArgumentException(
                    "Start time must be before end time.");

        if (slotDate == null)
            throw new IllegalArgumentException("Slot date cannot be null.");

        this.slotId = slotId.trim();
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotDate = slotDate;
        this.available = true;
    }

    public String getSlotId() {
        return slotId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean overlaps(TimeSlot other) {

        return this.slotDate.equals(other.slotDate)
                && this.startTime.isBefore(other.endTime)
                && other.startTime.isBefore(this.endTime);
    }

    public void displaySlot() {

        System.out.println("Slot ID: " + slotId);
        System.out.println("Date: " + slotDate);
        System.out.println("Time: " + startTime + " - " + endTime);

        System.out.println("Status: "
                + (available ? "AVAILABLE" : "BOOKED"));
    }
}
