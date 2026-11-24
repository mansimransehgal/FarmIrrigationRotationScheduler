
package com.farm.main;

import java.time.LocalTime;

import com.farm.model.*;
import com.farm.scheduler.*;

public class TestScheduler {

    public static void main(String[] args) {

        IrrigationScheduler scheduler =
                new IrrigationScheduler();

        // STEP 1: Create fields
        Field f1 = new Field(
                "F101", "North Field", 2.5, 500, "LOW");

        Field f2 = new Field(
                "F102", "South Field", 3.0, 700, "HIGH");

        Field f3 = new Field(
                "F103", "East Field", 1.5, 300, "MEDIUM");

        Field f4 = new Field(
                "F104", "West Field", 2.0, 400, "HIGH");

        // STEP 2: Register fields in HashMap
        scheduler.registerField(f1);
        scheduler.registerField(f2);
        scheduler.registerField(f3);
        scheduler.registerField(f4);

        // STEP 3: Create requests
        IrrigationRequest r1 =
                new IrrigationRequest("R001", f1, "LOW");

        IrrigationRequest r2 =
                new IrrigationRequest("R002", f2, "HIGH");

        IrrigationRequest r3 =
                new IrrigationRequest("R003", f3, "MEDIUM");

        IrrigationRequest r4 =
                new IrrigationRequest("R004", f4, "HIGH");

        // STEP 4: Submit requests
        scheduler.addRequest(r1);
        scheduler.addRequest(r2);
        scheduler.addRequest(r3);
        scheduler.addRequest(r4);

        // STEP 5: Add time slots
        scheduler.addTimeSlot(new TimeSlot(
                "T01", LocalTime.of(6, 0),
                LocalTime.of(7, 0)));

        scheduler.addTimeSlot(new TimeSlot(
                "T02", LocalTime.of(7, 0),
                LocalTime.of(8, 0)));

        scheduler.addTimeSlot(new TimeSlot(
                "T03", LocalTime.of(8, 0),
                LocalTime.of(9, 0)));

        scheduler.addTimeSlot(new TimeSlot(
                "T04", LocalTime.of(9, 0),
                LocalTime.of(10, 0)));

        // STEP 6: Display registered fields
        scheduler.displayRegisteredFields();

        // STEP 7: Allocate requests
        scheduler.allocateSlots();

        // STEP 8: Display results
        scheduler.displayWaterUsage();

        scheduler.displayAllocations();

        scheduler.displayPendingRequests();

        scheduler.displayAvailableSlots();
    }
}
