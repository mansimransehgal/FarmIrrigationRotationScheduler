
package com.farm.scheduler;

import java.util.*;

import com.farm.model.*;

public class IrrigationScheduler {

    // HashMap: Store fields using their unique IDs
    private Map<String, Field> fields = new HashMap<>();

    // Queue: Store pending irrigation requests
    private Queue<IrrigationRequest> requestQueue =
            new LinkedList<>();

    // Store available time slots
    private List<TimeSlot> timeSlots = new ArrayList<>();

    // Store successful allocations
    private List<Allocation> allocations = new ArrayList<>();

    // Water management
    private double dailyWaterLimit = 2000;
    private double allocatedWater = 0;

    private int allocationCounter = 1;

    // Register a field in HashMap
    public void registerField(Field field) {

        if (fields.containsKey(field.getFieldId())) {
            System.out.println("Field ID already exists.");
            return;
        }

        fields.put(field.getFieldId(), field);

        System.out.println(
                field.getFieldName() + " registered successfully."
        );
    }

    // Retrieve a field using its ID
    public Field getField(String fieldId) {
        return fields.get(fieldId);
    }

    // Add irrigation request
    public void addRequest(IrrigationRequest request) {

        String fieldId = request.getField().getFieldId();

        if (!fields.containsKey(fieldId)) {
            System.out.println("Field not registered: " + fieldId);
            return;
        }

        requestQueue.offer(request);

        System.out.println(
                "Request " + request.getRequestId()
                + " added successfully."
        );
    }

    // Add time slot
    public void addTimeSlot(TimeSlot slot) {

        timeSlots.add(slot);

        System.out.println(
                "Time slot " + slot.getSlotId()
                + " added successfully."
        );
    }

    // Display pending requests
    public void displayPendingRequests() {

        System.out.println("\n=== PENDING REQUESTS ===");

        for (IrrigationRequest request : requestQueue) {

            System.out.println(
                    request.getRequestId()
                    + " - " + request.getPriority()
            );
        }
    }

    // Display available slots
    public void displayAvailableSlots() {

        System.out.println("\n=== AVAILABLE TIME SLOTS ===");

        for (TimeSlot slot : timeSlots) {

            if (slot.isAvailable()) {
                slot.displaySlot();
                System.out.println();
            }
        }
    }

    // Display priority order
    public void processRequestsByPriority() {

        List<IrrigationRequest> sortedRequests =
                new ArrayList<>(requestQueue);

        sortedRequests.sort(new PriorityComparator());

        System.out.println("\n=== PRIORITY ORDER ===");

        for (IrrigationRequest request : sortedRequests) {

            System.out.println(
                    request.getRequestId()
                    + " - " + request.getPriority()
            );
        }
    }

    // Allocate irrigation slots
    public void allocateSlots() {

        List<IrrigationRequest> sortedRequests =
                new ArrayList<>(requestQueue);

        sortedRequests.sort(new PriorityComparator());

        // Keep only requests that could not be allocated
        Queue<IrrigationRequest> pendingRequests =
                new LinkedList<>();

        System.out.println("\n=== IRRIGATION ALLOCATION ===");

        for (IrrigationRequest request : sortedRequests) {

            boolean allocated = false;

            double requiredWater =
                    request.getField().getWaterRequirement();

            // Check water availability
            if (allocatedWater + requiredWater > dailyWaterLimit) {

                System.out.println(
                        request.getRequestId()
                        + " remains PENDING: Insufficient water."
                );

                pendingRequests.offer(request);
                continue;
            }

            // Find an available slot
            for (TimeSlot slot : timeSlots) {

                if (slot.isAvailable()) {

                    slot.setAvailable(false);

                    Allocation allocation = new Allocation(
                            "A" + allocationCounter++,
                            request,
                            slot,
                            requiredWater
                    );

                    allocations.add(allocation);

                    allocatedWater += requiredWater;

                    request.setStatus("APPROVED");

                    allocated = true;

                    System.out.println(
                            request.getRequestId()
                            + " allocated successfully."
                    );

                    allocation.displayAllocation();

                    System.out.println();

                    break;
                }
            }

            if (!allocated) {

                System.out.println(
                        request.getRequestId()
                        + " remains PENDING: No available slot."
                );

                pendingRequests.offer(request);
            }
        }

        // Replace queue with requests still pending
        requestQueue = pendingRequests;
    }

    // Display water usage
    public void displayWaterUsage() {

        System.out.println("\n=== DAILY WATER USAGE ===");

        System.out.println(
                "Daily Limit: " + dailyWaterLimit + " litres"
        );

        System.out.println(
                "Allocated: " + allocatedWater + " litres"
        );

        System.out.println(
                "Remaining: "
                + (dailyWaterLimit - allocatedWater) + " litres"
        );
    }

    // Display all allocations
    public void displayAllocations() {

        System.out.println("\n=== ALL ALLOCATIONS ===");

        for (Allocation allocation : allocations) {
            allocation.displayAllocation();
            System.out.println();
        }
    }

    // Display registered fields
    public void displayRegisteredFields() {

        System.out.println("\n=== REGISTERED FIELDS ===");

        for (Field field : fields.values()) {
            field.displayFieldDetails();
            System.out.println();
        }
    }
}
