
package com.farm.model;

import java.time.LocalTime;

public class TimeSlot {

    private String slotId;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean available;

    // Constructor
    public TimeSlot(String slotId, LocalTime startTime,
                    LocalTime endTime) {

        if (slotId == null || slotId.trim().isEmpty())
            throw new IllegalArgumentException("Slot ID cannot be empty.");

        if (startTime == null || endTime == null)
            throw new IllegalArgumentException("Time cannot be null.");

        if (!startTime.isBefore(endTime))
            throw new IllegalArgumentException(
                    "Start time must be before end time.");

        this.slotId = slotId.trim();
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = true;
    }

    // Getters
    public String getSlotId() {
        return slotId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isAvailable() {
        return available;
    }

    // Update availability
    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Check whether two time slots overlap
    public boolean overlaps(TimeSlot other) {

        return this.startTime.isBefore(other.endTime)
                && other.startTime.isBefore(this.endTime);
    }

    // Display slot information
    public void displaySlot() {

        System.out.println("Slot ID: " + slotId);
        System.out.println("Time: " + startTime + " - " + endTime);

        System.out.println("Status: "
                + (available ? "AVAILABLE" : "BOOKED"));
    }
}
