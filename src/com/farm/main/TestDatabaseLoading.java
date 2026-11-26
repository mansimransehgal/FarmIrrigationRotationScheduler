
package com.farm.main;

import com.farm.scheduler.IrrigationScheduler;
import com.farm.reports.ReportService;

public class TestDatabaseLoading {

    public static void main(String[] args) {

        // Creating scheduler automatically loads MySQL data
        IrrigationScheduler scheduler =
                new IrrigationScheduler();

        System.out.println("\n===== DATABASE LOADING TEST =====");

        // Display registered fields
        System.out.println("\nTotal Fields: "
                + scheduler.getRegisteredFields().size());

        scheduler.displayRegisteredFields();

        // Display pending requests
        System.out.println("\nTotal Pending Requests: "
                + scheduler.getPendingRequests().size());

        scheduler.displayPendingRequests();

        // Display allocations
        System.out.println("\nTotal Loaded Allocations: "
                + scheduler.getAllocations().size());

        scheduler.displayAllocations();

        // Generate reports using loaded allocations
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

        System.out.println("\n===== DATABASE TEST COMPLETE =====");
    }
}
