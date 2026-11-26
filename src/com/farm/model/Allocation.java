
package com.farm.model;

import java.time.LocalDate;

public class Allocation {

    private String allocationId;
    private IrrigationRequest request;
    private TimeSlot timeSlot;
    private double allocatedWater;
    private LocalDate allocationDate;

    // Constructor for a new allocation
    public Allocation(String allocationId,
                      IrrigationRequest request,
                      TimeSlot timeSlot,
                      double allocatedWater) {

        this(allocationId, request, timeSlot,
                allocatedWater,
                timeSlot == null ? null : timeSlot.getSlotDate());
    }

    // Constructor for restoring an allocation from MySQL
    public Allocation(String allocationId,
                      IrrigationRequest request,
                      TimeSlot timeSlot,
                      double allocatedWater,
                      LocalDate allocationDate) {

        if (allocationId == null || allocationId.trim().isEmpty())
            throw new IllegalArgumentException("Invalid allocation ID.");

        if (request == null || timeSlot == null)
            throw new IllegalArgumentException("Request and slot cannot be null.");

        if (allocatedWater <= 0 || !Double.isFinite(allocatedWater))
            throw new IllegalArgumentException("Water must be positive.");

        if (allocationDate == null)
            throw new IllegalArgumentException("Allocation date cannot be null.");

        this.allocationId = allocationId;
        this.request = request;
        this.timeSlot = timeSlot;
        this.allocatedWater = allocatedWater;
        this.allocationDate = allocationDate;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public IrrigationRequest getRequest() {
        return request;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public double getAllocatedWater() {
        return allocatedWater;
    }

    public LocalDate getAllocationDate() {
        return allocationDate;
    }

    public void displayAllocation() {
        System.out.println("Allocation ID: " + allocationId);
        System.out.println("Request ID: " + request.getRequestId());
        System.out.println("Field: " + request.getField().getFieldName());
        System.out.println("Time Slot: " + timeSlot.getStartTime()
                + " - " + timeSlot.getEndTime());
        System.out.println("Allocated Water: " + allocatedWater + " litres");
        System.out.println("Allocation Date: " + allocationDate);
    }
}
