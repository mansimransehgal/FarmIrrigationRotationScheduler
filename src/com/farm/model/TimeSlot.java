
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

        this.slotId = slotId;
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

    // Display slot information
    public void displaySlot() {

        System.out.println("Slot ID: " + slotId);
        System.out.println("Time: " + startTime
                           + " - " + endTime);

        System.out.println("Status: "
                           + (available ? "AVAILABLE" : "BOOKED"));
    }
}
