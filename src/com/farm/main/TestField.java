
package com.farm.main;

import java.time.LocalTime;

import com.farm.model.Field;
import com.farm.model.IrrigationRequest;
import com.farm.model.TimeSlot;
import com.farm.model.Allocation;

public class TestField {

    public static void main(String[] args) {

        // Create a field
        Field field = new Field(
            "F101", "North Field", 2.5, 500, "HIGH"
        );

        // Create an irrigation request
        IrrigationRequest request =
            new IrrigationRequest("R001", field, "HIGH");

        // Create a time slot
        TimeSlot slot = new TimeSlot(
            "T01",
            LocalTime.of(6, 0),
            LocalTime.of(7, 0)
        );

        // Create an allocation
        Allocation allocation = new Allocation(
            "A001",
            request,
            slot,
            500
        );

        // Display all details
        System.out.println("=== IRRIGATION ALLOCATION ===");

        allocation.displayAllocation();

        System.out.println("\n=== TIME SLOT STATUS ===");

        slot.displaySlot();
    }
}
