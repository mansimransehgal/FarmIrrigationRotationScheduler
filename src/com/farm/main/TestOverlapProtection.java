
package com.farm.main;

import java.time.LocalTime;

import com.farm.model.*;
import com.farm.scheduler.IrrigationScheduler;
import com.farm.database.AllocationDAO;

public class TestOverlapProtection {

    public static void main(String[] args) {

        // Load existing test data from MySQL
        IrrigationScheduler scheduler =
                new IrrigationScheduler();

        // Create a fresh field and request
        Field field = new Field(
                "F960", "Test Farm", 2.0, 300, "HIGH"
        );

        IrrigationRequest request =
                new IrrigationRequest("R960", field, "HIGH");

        // Create an overlapping time slot
        TimeSlot slot = new TimeSlot(
                "T960",
                LocalTime.of(6, 30),
                LocalTime.of(7, 30)
        );

        // Register field and request
        scheduler.registerField(field);
        scheduler.addRequest(request);

        // Create allocation
        Allocation allocation = new Allocation(
                "A960",
                request,
                slot,
                300
        );

        AllocationDAO dao = new AllocationDAO();

        System.out.println("\n===== OVERLAP TEST =====");

        boolean result = dao.saveAllocation(allocation);

        if (result) {
            System.out.println(
                    "TEST FAILED: Overlapping allocation accepted."
            );
        } else {
            System.out.println(
                    "TEST PASSED: Overlapping allocation rejected."
            );
        }
    }
}
