
package com.farm.main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.farm.model.*;
import com.farm.scheduler.*;
import com.farm.reports.ReportService;

public class TestScheduler {

    public static void main(String[] args) {

        IrrigationScheduler scheduler =
                new IrrigationScheduler();

        ReportService reportService =
                new ReportService();

        // Unique IDs for this demonstration
        String tag = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        // Use a separate date for demonstration
        LocalDate demoDate = LocalDate.now().plusDays(1);

        System.out.println("DEMONSTRATION DATE: " + demoDate);

        // STEP 1: Create fields
        Field f1 = new Field(
                "F" + tag + "1", "North Field",
                2.5, 500, "LOW");

        Field f2 = new Field(
                "F" + tag + "2", "South Field",
                3.0, 700, "HIGH");

        Field f3 = new Field(
                "F" + tag + "3", "East Field",
                1.5, 300, "MEDIUM");

        Field f4 = new Field(
                "F" + tag + "4", "West Field",
                2.0, 400, "HIGH");

        // STEP 2: Register fields
        scheduler.registerField(f1);
        scheduler.registerField(f2);
        scheduler.registerField(f3);
        scheduler.registerField(f4);

        // STEP 3: Create irrigation requests
        IrrigationRequest r1 =
                new IrrigationRequest("R" + tag + "1", f1, "LOW");

        IrrigationRequest r2 =
                new IrrigationRequest("R" + tag + "2", f2, "HIGH");

        IrrigationRequest r3 =
                new IrrigationRequest("R" + tag + "3", f3, "MEDIUM");

        IrrigationRequest r4 =
                new IrrigationRequest("R" + tag + "4", f4, "HIGH");

        // STEP 4: Submit requests
        scheduler.addRequest(r1);
        scheduler.addRequest(r2);
        scheduler.addRequest(r3);
        scheduler.addRequest(r4);

        // STEP 5: Add time slots for demonstration date
        scheduler.addTimeSlot(new TimeSlot(
                "T" + tag + "1",
                LocalTime.of(6, 0),
                LocalTime.of(7, 0),
                demoDate));

        scheduler.addTimeSlot(new TimeSlot(
                "T" + tag + "2",
                LocalTime.of(7, 0),
                LocalTime.of(8, 0),
                demoDate));

        scheduler.addTimeSlot(new TimeSlot(
                "T" + tag + "3",
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                demoDate));

        scheduler.addTimeSlot(new TimeSlot(
                "T" + tag + "4",
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                demoDate));

        // STEP 6: Display registered fields
        scheduler.displayRegisteredFields();

        // STEP 7: Display priority order
        scheduler.processRequestsByPriority();

        // STEP 8: Allocate irrigation requests
        scheduler.allocateSlots();

        // STEP 9: Display allocation results
        scheduler.displayAllocations();

        // STEP 10: Display remaining pending requests
        scheduler.displayPendingRequests();

        // STEP 11: Display available slots
        scheduler.displayAvailableSlots();

        // STEP 12: Display water usage for demonstration date
        System.out.println("\n=== DEMONSTRATION DATE WATER USAGE ===");
        System.out.println("Date: " + demoDate);
        System.out.println("Allocated: "
                + scheduler.getWaterUsage(demoDate) + " litres");
        System.out.println("Daily limit: 2000 litres");

        // STEP 13: Generate reports
        reportService.generateFieldWiseSchedule(
                scheduler.getAllocations());

        reportService.generatePendingRequestReport(
                scheduler.getPendingRequests());

        reportService.generateWeeklyWaterReport(
                scheduler.getAllocations());

        System.out.println("\n=== DEMONSTRATION COMPLETED ===");
    }
}
