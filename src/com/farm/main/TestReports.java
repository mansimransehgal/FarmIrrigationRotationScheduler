
package com.farm.main;

import java.time.LocalTime;

import com.farm.model.*;
import com.farm.scheduler.IrrigationScheduler;
import com.farm.reports.ReportService;

public class TestReports {

    public static void main(String[] args) {

        IrrigationScheduler scheduler =
                new IrrigationScheduler();

        // 1. Register fields
        Field f1 = new Field(
                "F950", "North Farm", 2.0, 400, "HIGH"
        );

        Field f2 = new Field(
                "F951", "South Farm", 3.0, 600, "MEDIUM"
        );

        scheduler.registerField(f1);
        scheduler.registerField(f2);

        // 2. Add irrigation requests
        scheduler.addRequest(
                new IrrigationRequest("R950", f1, "HIGH")
        );

        scheduler.addRequest(
                new IrrigationRequest("R951", f2, "MEDIUM")
        );

        // 3. Add time slots
        scheduler.addTimeSlot(new TimeSlot(
                "T950",
                LocalTime.of(6, 0),
                LocalTime.of(7, 0)
        ));

        scheduler.addTimeSlot(new TimeSlot(
                "T951",
                LocalTime.of(7, 0),
                LocalTime.of(8, 0)
        ));

        // 4. Allocate irrigation slots
        scheduler.allocateSlots();

        // 5. Generate reports
        ReportService reports = new ReportService();

        System.out.println("\n===== GENERATING REPORTS =====");

        reports.generateFieldWiseSchedule(
                scheduler.getAllocations()
        );

        reports.generatePendingRequestReport(
                scheduler.getPendingRequests()
        );

        reports.generateDailyWaterReport(
                scheduler.getAllocations()
        );

        reports.generateWeeklyWaterReport(
                scheduler.getAllocations()
        );
    }
}
