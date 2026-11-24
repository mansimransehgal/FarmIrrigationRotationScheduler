
package com.farm.main;

import java.time.LocalTime;

import com.farm.model.*;
import com.farm.scheduler.*;

public class TestScheduler {

    public static void main(String[] args) {

        IrrigationScheduler scheduler =
                new IrrigationScheduler();

        // Create fields
        Field f1 = new Field(
                "F101", "North Field", 2.5, 500, "LOW");

        Field f2 = new Field(
                "F102", "South Field", 3.0, 700, "HIGH");

        Field f3 = new Field(
                "F103", "East Field", 1.5, 300, "MEDIUM");

        Field f4 = new Field(
                "F104", "West Field", 2.0, 400, "HIGH");

        // Create irrigation requests
        IrrigationRequest r1 =
                new IrrigationRequest("R001", f1, "LOW");

        IrrigationRequest r2 =
                new IrrigationRequest("R002", f2, "HIGH");

        IrrigationRequest r3 =
                new IrrigationRequest("R003", f3, "MEDIUM");

        IrrigationRequest r4 =
                new IrrigationRequest("R004", f4, "HIGH");

        // Add requests
        scheduler.addRequest(r1);
        scheduler.addRequest(r2);
        scheduler.addRequest(r3);
        scheduler.addRequest(r4);

        // Add time slots
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

        // Allocate irrigation
        scheduler.allocateSlots();

        // Display water usage
        scheduler.displayWaterUsage();

        // Display available slots
        scheduler.displayAvailableSlots();
    }
}
