
package com.farm.main;

import java.time.LocalDate;
import java.time.LocalTime;

import com.farm.model.*;
import com.farm.scheduler.IrrigationScheduler;
import com.farm.reports.ReportService;

public class TestSchedulerIntegration {

    public static void main(String[] args) {

        System.out.println("===== FARM IRRIGATION INTEGRATION TEST =====");

        // 1. Load saved database records
        IrrigationScheduler scheduler =
                new IrrigationScheduler();

        // 2. Display registered fields
        System.out.println("\n--- REGISTERED FIELDS ---");
        System.out.println("Total: "
                + scheduler.getRegisteredFields().size());

        // 3. Display pending requests
        System.out.println("\n--- PENDING REQUESTS ---");
        scheduler.displayPendingRequests();

        // 4. Display available time slots
        System.out.println("\n--- AVAILABLE SLOTS ---");
        scheduler.displayAvailableSlots();

        // 5. Display loaded allocations
        System.out.println("\n--- SAVED ALLOCATIONS ---");
        scheduler.displayAllocations();

        // 6. Verify daily water tracking
        System.out.println("\n--- DAILY WATER USAGE ---");
        scheduler.displayWaterUsage();

        // 7. Generate reports
        ReportService reports = new ReportService();

        reports.generateFieldWiseSchedule(
                scheduler.getAllocations()
        );

        reports.generateDailyWaterReport(
                scheduler.getAllocations()
        );

        reports.generateWeeklyWaterReport(
                scheduler.getAllocations()
        );

        System.out.println("\n===== TEST COMPLETED =====");
    }
}
	