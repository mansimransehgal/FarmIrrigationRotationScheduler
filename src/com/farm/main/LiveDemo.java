
package com.farm.main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.farm.model.*;
import com.farm.scheduler.*;
import com.farm.reports.ReportService;

public class LiveDemo {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println(" FARM IRRIGATION ROTATION SCHEDULER");
        System.out.println(" LIVE PROJECT DEMONSTRATION");
        System.out.println("==================================");

        IrrigationScheduler scheduler =
                new IrrigationScheduler();

        ReportService reports = new ReportService();

        String tag = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        LocalDate demoDate = LocalDate.now().plusDays(1);

        System.out.println("\nDEMO DATE: " + demoDate);

        // =================================
        // 1. FIELD REGISTRATION
        // =================================

        System.out.println("\n1. FIELD REGISTRATION");

        Field f1 = new Field(
                "F" + tag + "01",
                "North Farm", 2.5, 500, "LOW");

        Field f2 = new Field(
                "F" + tag + "02",
                "South Farm", 3.0, 700, "HIGH");

        Field f3 = new Field(
                "F" + tag + "03",
                "East Farm", 1.5, 300, "MEDIUM");

        Field f4 = new Field(
                "F" + tag + "04",
                "West Farm", 2.0, 700, "HIGH");

        scheduler.registerField(f1);
        scheduler.registerField(f2);
        scheduler.registerField(f3);
        scheduler.registerField(f4);

        // =================================
        // 2. IRRIGATION REQUESTS
        // =================================

        System.out.println("\n2. CREATING IRRIGATION REQUESTS");

        IrrigationRequest r1 =
                new IrrigationRequest(
                        "R" + tag + "01", f1, "LOW");

        IrrigationRequest r2 =
                new IrrigationRequest(
                        "R" + tag + "02", f2, "HIGH");

        IrrigationRequest r3 =
                new IrrigationRequest(
                        "R" + tag + "03", f3, "MEDIUM");

        IrrigationRequest r4 =
                new IrrigationRequest(
                        "R" + tag + "04", f4, "HIGH");

        scheduler.addRequest(r1);
        scheduler.addRequest(r2);
        scheduler.addRequest(r3);
        scheduler.addRequest(r4);

        // =================================
        // 3. TIME SLOT MANAGEMENT
        // =================================

        System.out.println("\n3. ADDING TIME SLOTS");

        TimeSlot t1 = new TimeSlot(
                "T" + tag + "01",
                LocalTime.of(6, 0),
                LocalTime.of(7, 0),
                demoDate);

        TimeSlot t2 = new TimeSlot(
                "T" + tag + "02",
                LocalTime.of(7, 0),
                LocalTime.of(8, 0),
                demoDate);

        TimeSlot t3 = new TimeSlot(
                "T" + tag + "03",
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                demoDate);

        TimeSlot t4 = new TimeSlot(
                "T" + tag + "04",
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                demoDate);

        scheduler.addTimeSlot(t1);

        // =================================
        // 4. OVERLAP PREVENTION
        // =================================

        System.out.println("\n4. TESTING OVERLAPPING TIME SLOT");

        TimeSlot overlappingSlot = new TimeSlot(
                "T" + tag + "05",
                LocalTime.of(6, 30),
                LocalTime.of(7, 30),
                demoDate);

        scheduler.addTimeSlot(overlappingSlot);

        // Add the remaining non-overlapping slots
        scheduler.addTimeSlot(t2);
        scheduler.addTimeSlot(t3);
        scheduler.addTimeSlot(t4);

        // =================================
        // 5. PRIORITY-BASED SCHEDULING
        // =================================

        System.out.println("\n5. PRIORITY-BASED REQUEST ORDER");

        scheduler.processRequestsByPriority();

        // =================================
        // 6. IRRIGATION ALLOCATION
        // =================================

        System.out.println("\n6. ALLOCATING IRRIGATION SLOTS");

        scheduler.allocateSlots();

        // =================================
        // 7. WATER CONSUMPTION
        // =================================

        System.out.println("\n7. DAILY WATER CONSUMPTION");

        System.out.println("Date: " + demoDate);

        System.out.println(
                "Water Used: "
                + scheduler.getWaterUsage(demoDate)
                + " litres");

        System.out.println("Daily Limit: 2000 litres");

        System.out.println(
                "Remaining: "
                + (2000 - scheduler.getWaterUsage(demoDate))
                + " litres");

        // =================================
        // 8. DISPLAY RESULTS
        // =================================

        System.out.println("\n8. FINAL ALLOCATIONS");

        scheduler.displayAllocations();

        System.out.println("\nPENDING REQUESTS");

        scheduler.displayPendingRequests();

        System.out.println("\nAVAILABLE TIME SLOTS");

        scheduler.displayAvailableSlots();

        // =================================
        // 9. REPORT GENERATION
        // =================================

        System.out.println("\n9. GENERATING REPORTS");

        reports.generateFieldWiseSchedule(
                scheduler.getAllocations());

        reports.generatePendingRequestReport(
                scheduler.getPendingRequests());

        reports.generateDailyWaterReport(
                scheduler.getAllocations());

        reports.generateWeeklyWaterReport(
                scheduler.getAllocations());

        System.out.println("\n==================================");
        System.out.println(" LIVE DEMONSTRATION COMPLETED");
        System.out.println("==================================");
    }
}
