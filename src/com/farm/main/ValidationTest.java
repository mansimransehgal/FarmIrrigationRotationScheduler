
package com.farm.main;

import java.time.LocalTime;

import com.farm.model.*;
import com.farm.scheduler.*;

public class ValidationTest {

    public static void main(String[] args) {

        IrrigationScheduler scheduler =
                new IrrigationScheduler();

        System.out.println("=== TEST 1: FIELD VALIDATION ===");

        try {
            Field invalidField = new Field(
                    "F101", "North Field",
                    -2.5, 500, "HIGH"
            );

        } catch (IllegalArgumentException e) {
            System.out.println("Error caught: " + e.getMessage());
        }

        System.out.println("\n=== TEST 2: FIELD REGISTRATION ===");

        Field f1 = new Field(
                "F101", "North Field", 2.5, 500, "HIGH");

        scheduler.registerField(f1);

        // Attempt duplicate registration
        scheduler.registerField(f1);

        System.out.println("\n=== TEST 3: TIME SLOT VALIDATION ===");

        TimeSlot t1 = new TimeSlot(
                "T01",
                LocalTime.of(6, 0),
                LocalTime.of(7, 0)
        );

        TimeSlot t2 = new TimeSlot(
                "T02",
                LocalTime.of(6, 30),
                LocalTime.of(7, 30)
        );

        TimeSlot t3 = new TimeSlot(
                "T03",
                LocalTime.of(7, 0),
                LocalTime.of(8, 0)
        );

        scheduler.addTimeSlot(t1);

        // Overlapping slot
        scheduler.addTimeSlot(t2);

        // Non-overlapping slot
        scheduler.addTimeSlot(t3);

        System.out.println("\n=== VALIDATION TEST COMPLETED ===");
    }
}
