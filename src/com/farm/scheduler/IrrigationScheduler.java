
package com.farm.scheduler;

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

import com.farm.model.*;
import com.farm.database.*;

public class IrrigationScheduler {

    // In-memory collections
    private Map<String, Field> fields = new HashMap<>();

    private Map<String, IrrigationRequest> requests = new HashMap<>();

    private Queue<IrrigationRequest> requestQueue = new LinkedList<>();

    private List<TimeSlot> timeSlots = new ArrayList<>();

    private Map<String, TimeSlot> slots = new HashMap<>();

    private List<Allocation> allocations = new ArrayList<>();

    // Track water consumption separately for each date
    private Map<LocalDate, Double> dailyWaterUsage = new HashMap<>();

    // Database access objects
    private FieldDAO fieldDAO = new FieldDAO();
    private RequestDAO requestDAO = new RequestDAO();
    private TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
    private AllocationDAO allocationDAO = new AllocationDAO();

    private double dailyWaterLimit = 2000;

    // Automatically load database records
    public IrrigationScheduler() {
        loadExistingData();
    }

    // Load existing records from MySQL
    private synchronized void loadExistingData() {

        System.out.println("\n=== LOADING DATABASE DATA ===");

        // Load fields
        List<Field> savedFields = fieldDAO.getAllFields();

        for (Field field : savedFields) {
            fields.put(field.getFieldId(), field);
        }

        System.out.println("Fields loaded: " + fields.size());

        // Load requests
        List<IrrigationRequest> savedRequests =
                requestDAO.getAllRequests(fields);

        for (IrrigationRequest request : savedRequests) {

            requests.put(request.getRequestId(), request);

            if (request.getStatus().equals("PENDING")) {
                requestQueue.offer(request);
            }
        }

        System.out.println("Requests loaded: " + requests.size());

        // Load time slots
        timeSlots = timeSlotDAO.getAllTimeSlots();

        for (TimeSlot slot : timeSlots) {
            slots.put(slot.getSlotId(), slot);
        }

        // Sort slots by date and start time
        timeSlots.sort(
            Comparator.comparing(TimeSlot::getSlotDate)
                      .thenComparing(TimeSlot::getStartTime)
        );

        System.out.println("Time slots loaded: " + timeSlots.size());

        // Load allocations
        allocations = allocationDAO.getAllAllocations(
                requests, slots
        );

        // Calculate water usage for each date
        dailyWaterUsage = allocations.stream()
            .collect(Collectors.groupingBy(
                Allocation::getAllocationDate,
                Collectors.summingDouble(
                    Allocation::getAllocatedWater
                )
            ));

        System.out.println("Allocations loaded: " + allocations.size());

        System.out.println("Today's water usage: "
                + getWaterUsage(LocalDate.now()) + " litres");

        System.out.println("=== DATABASE LOADING COMPLETE ===\n");
    }

    // Register field
    public synchronized void registerField(Field field) {

        if (field == null) {
            System.out.println("Invalid field.");
            return;
        }

        if (fields.containsKey(field.getFieldId())) {
            System.out.println("Field ID already exists.");
            return;
        }

        if (fieldDAO.saveField(field)) {

            fields.put(field.getFieldId(), field);

            System.out.println(
                field.getFieldName() + " registered successfully."
            );

        } else {
            System.out.println("Field registration failed.");
        }
    }

    public synchronized Field getField(String fieldId) {
        return fields.get(fieldId);
    }

    // Add irrigation request
    public synchronized void addRequest(IrrigationRequest request) {

        if (request == null) {
            System.out.println("Invalid irrigation request.");
            return;
        }

        String fieldId = request.getField().getFieldId();

        if (!fields.containsKey(fieldId)) {
            System.out.println("Field is not registered: " + fieldId);
            return;
        }

        if (requests.containsKey(request.getRequestId())) {
            System.out.println("Duplicate request ID.");
            return;
        }

        if (requestDAO.saveRequest(request)) {

            requests.put(request.getRequestId(), request);
            requestQueue.offer(request);

            System.out.println(
                "Request " + request.getRequestId()
                + " added successfully."
            );

        } else {
            System.out.println("Request could not be added.");
        }
    }

    // Add time slot
    public synchronized void addTimeSlot(TimeSlot slot) {

        if (slot == null) {
            System.out.println("Invalid time slot.");
            return;
        }

        if (slots.containsKey(slot.getSlotId())) {
            System.out.println("Duplicate time slot ID.");
            return;
        }

        for (TimeSlot existing : timeSlots) {

            if (existing.overlaps(slot)) {
                System.out.println(
                    "Time slot overlaps with " + existing.getSlotId()
                );
                return;
            }
        }

        if (timeSlotDAO.saveTimeSlot(slot)) {

            timeSlots.add(slot);
            slots.put(slot.getSlotId(), slot);

            timeSlots.sort(
                Comparator.comparing(TimeSlot::getSlotDate)
                          .thenComparing(TimeSlot::getStartTime)
            );

            System.out.println(
                "Time slot " + slot.getSlotId()
                + " added successfully."
            );

        } else {
            System.out.println("Time slot could not be added.");
        }
    }

    // Get water consumption for a particular date
    public synchronized double getWaterUsage(LocalDate date) {
        return dailyWaterUsage.getOrDefault(date, 0.0);
    }

    // Display pending requests
    public void displayPendingRequests() {

        System.out.println("\n=== PENDING REQUESTS ===");

        for (IrrigationRequest request : requestQueue) {
            System.out.println(
                request.getRequestId() + " - " + request.getPriority()
            );
        }
    }

    // Display available time slots
    public void displayAvailableSlots() {

        System.out.println("\n=== AVAILABLE TIME SLOTS ===");

        for (TimeSlot slot : timeSlots) {

            if (slot.isAvailable()) {
                slot.displaySlot();
                System.out.println();
            }
        }
    }

    // Process requests according to priority
    public void processRequestsByPriority() {

        List<IrrigationRequest> sortedRequests =
                new ArrayList<>(requestQueue);

        sortedRequests.sort(new PriorityComparator());

        System.out.println("\n=== PRIORITY ORDER ===");

        for (IrrigationRequest request : sortedRequests) {
            System.out.println(
                request.getRequestId() + " - " + request.getPriority()
            );
        }
    }

    // Allocate time slots
    public synchronized void allocateSlots() {

        List<IrrigationRequest> sortedRequests =
                new ArrayList<>(requestQueue);

        sortedRequests.sort(new PriorityComparator());

        Queue<IrrigationRequest> pendingRequests =
                new LinkedList<>();

        System.out.println("\n=== IRRIGATION ALLOCATION ===");

        for (IrrigationRequest request : sortedRequests) {

            boolean allocated = false;

            double requiredWater =
                    request.getField().getWaterRequirement();

            // Find a suitable available slot
            for (TimeSlot slot : timeSlots) {

                if (!slot.isAvailable()) {
                    continue;
                }

                LocalDate allocationDate = slot.getSlotDate();

                double currentUsage =
                        getWaterUsage(allocationDate);

                // Check daily water limit for this date
                if (currentUsage + requiredWater > dailyWaterLimit) {
                    continue;
                }

                String allocationId = "A"
                        + UUID.randomUUID()
                        .toString().substring(0, 8);

                Allocation allocation = new Allocation(
                        allocationId,
                        request,
                        slot,
                        requiredWater
                );

                boolean saved =
                        allocationDAO.saveAllocation(allocation);

                if (saved) {

                    allocations.add(allocation);

                    dailyWaterUsage.put(
                        allocationDate,
                        currentUsage + requiredWater
                    );

                    slot.setAvailable(false);

                    request.setStatus("APPROVED");

                    allocated = true;

                    System.out.println(
                        request.getRequestId()
                        + " allocated successfully."
                    );

                    allocation.displayAllocation();

                    System.out.println();

                } else {

                    System.out.println(
                        request.getRequestId()
                        + " could not be allocated to slot "
                        + slot.getSlotId()
                    );
                }

                if (allocated) {
                    break;
                }
            }

            if (!allocated) {

                System.out.println(
                    request.getRequestId()
                    + " remains PENDING: No suitable slot or water available."
                );

                pendingRequests.offer(request);
            }
        }

        requestQueue = pendingRequests;
    }

    // Display daily water usage
    public void displayWaterUsage() {

        LocalDate today = LocalDate.now();

        double used = getWaterUsage(today);

        System.out.println("\n=== DAILY WATER USAGE ===");

        System.out.println("Date: " + today);
        System.out.println("Daily Limit: " + dailyWaterLimit + " litres");
        System.out.println("Allocated: " + used + " litres");
        System.out.println("Remaining: "
                + (dailyWaterLimit - used) + " litres");
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

    // Get copies of collections
    public List<Allocation> getAllocations() {
        return new ArrayList<>(allocations);
    }

    public List<IrrigationRequest> getPendingRequests() {
        return new ArrayList<>(requestQueue);
    }

    public List<Field> getRegisteredFields() {
        return new ArrayList<>(fields.values());
    }
}
