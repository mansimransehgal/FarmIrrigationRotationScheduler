
package com.farm.scheduler;

import java.util.*;

import java.util.ArrayList;
import com.farm.model.Field;
import com.farm.model.IrrigationRequest;
import com.farm.model.TimeSlot;
import com.farm.model.Allocation;

public class IrrigationScheduler {
	
	private double dailyWaterLimit = 2000;

	private double allocatedWater = 0;

	private int allocationCounter = 1;

    // Queue to store irrigation requests
    private Queue<IrrigationRequest> requestQueue =
            new LinkedList<>();

    // Store available time slots
    private List<TimeSlot> timeSlots =
            new ArrayList<>();

    // Store completed allocations
    private List<Allocation> allocations =
            new ArrayList<>();

    // Register an irrigation request
    public void addRequest(IrrigationRequest request) {

        requestQueue.offer(request);

        System.out.println("Request "
                + request.getRequestId()
                + " added successfully.");
    }

    // Add an available time slot
    public void addTimeSlot(TimeSlot slot) {

        timeSlots.add(slot);

        System.out.println("Time slot "
                + slot.getSlotId()
                + " added successfully.");
    }

    // Display pending requests
    public void displayPendingRequests() {

        System.out.println("\n=== PENDING REQUESTS ===");

        for (IrrigationRequest request : requestQueue) {

            System.out.println(
                    request.getRequestId()
                    + " - "
                    + request.getPriority()
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

     // Copy queue into a list
     List<IrrigationRequest> sortedRequests =
             new ArrayList<>(requestQueue);

     // Sort using our custom Comparator
     sortedRequests.sort(new PriorityComparator());

     System.out.println("\n=== PRIORITY-BASED REQUEST ORDER ===");

     for (IrrigationRequest request : sortedRequests) {

         System.out.println(
                 request.getRequestId()
                 + " - "
                 + request.getPriority()
         );
     }
 }
 

//Allocate available time slots to irrigation requests
public void allocateSlots() {

  List<IrrigationRequest> sortedRequests =
          new ArrayList<>(requestQueue);

  // Sort requests according to priority
  sortedRequests.sort(new PriorityComparator());

  System.out.println("\n=== IRRIGATION ALLOCATION ===");

  for (IrrigationRequest request : sortedRequests) {

      boolean allocated = false;

      double requiredWater =
              request.getField().getWaterRequirement();

      // Check daily water availability
      if (allocatedWater + requiredWater > dailyWaterLimit) {

          System.out.println(
                  request.getRequestId()
                  + " cannot be allocated: Insufficient water."
          );

          continue;
      }

      // Find an available time slot
      for (TimeSlot slot : timeSlots) {

          if (slot.isAvailable()) {

              // Mark slot as booked
              slot.setAvailable(false);

              // Create allocation
              Allocation allocation = new Allocation(
                      "A" + allocationCounter++,
                      request,
                      slot,
                      requiredWater
              );

              allocations.add(allocation);

              // Update water consumption
              allocatedWater += requiredWater;

              // Update request status
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
                  + " remains PENDING: No available time slot."
          );
      }
  }
}

//Display total water consumption
public void displayWaterUsage() {

  System.out.println("\n=== DAILY WATER USAGE ===");

  System.out.println(
          "Daily Water Limit: " + dailyWaterLimit + " litres"
  );

  System.out.println(
          "Allocated Water: " + allocatedWater + " litres"
  );

  System.out.println(
          "Remaining Water: "
          + (dailyWaterLimit - allocatedWater) + " litres"
  );
}


}
