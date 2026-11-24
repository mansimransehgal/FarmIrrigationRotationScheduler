
package com.farm.model;

public class Allocation {

    private String allocationId;
    private IrrigationRequest request;
    private TimeSlot timeSlot;
    private double allocatedWater;

    // Constructor
    public Allocation(String allocationId,
                      IrrigationRequest request,
                      TimeSlot timeSlot,
                      double allocatedWater) {

        this.allocationId = allocationId;
        this.request = request;
        this.timeSlot = timeSlot;
        this.allocatedWater = allocatedWater;
    }

    // Getters
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

    // Display allocation details
    public void displayAllocation() {

        System.out.println("Allocation ID: " + allocationId);

        System.out.println("Request ID: "
                           + request.getRequestId());

        System.out.println("Field: "
                           + request.getField().getFieldName());

        System.out.println("Time Slot: "
                           + timeSlot.getStartTime()
                           + " - " + timeSlot.getEndTime());

        System.out.println("Allocated Water: "
                           + allocatedWater + " litres");
    }
}
