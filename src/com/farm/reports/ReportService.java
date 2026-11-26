
package com.farm.reports;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import com.farm.model.*;
import com.farm.scheduler.PriorityComparator;

public class ReportService {

    // 1. Field-wise schedule
    public void generateFieldWiseSchedule(List<Allocation> allocations) {

        System.out.println("\n===== FIELD-WISE SCHEDULE =====");

        Map<String, List<Allocation>> grouped =
                allocations.stream()
                .collect(Collectors.groupingBy(
                    a -> a.getRequest().getField().getFieldName(),
                    TreeMap::new,
                    Collectors.toList()
                ));

        grouped.forEach((fieldName, fieldAllocations) -> {

            System.out.println("\nField: " + fieldName);

            fieldAllocations.forEach(a -> {
                System.out.println(
                    "Time: " + a.getTimeSlot().getStartTime()
                    + " - " + a.getTimeSlot().getEndTime()
                );

                System.out.println(
                    "Water: " + a.getAllocatedWater() + " litres"
                );
            });
        });
    }

    // 2. Pending request report
    public void generatePendingRequestReport(
            List<IrrigationRequest> requests) {

        System.out.println("\n===== PENDING REQUEST REPORT =====");

        requests.stream()
            .filter(r -> r.getStatus().equals("PENDING"))
            .sorted(new PriorityComparator())
            .forEach(r -> System.out.println(
                "Request: " + r.getRequestId()
                + " | Field: " + r.getField().getFieldName()
                + " | Priority: " + r.getPriority()
            ));
    }

    // 3. Daily water report
    public void generateDailyWaterReport(List<Allocation> allocations) {

        double totalWater = allocations.stream()
            .mapToDouble(Allocation::getAllocatedWater)
            .sum();

        long totalAllocations = allocations.stream().count();

        System.out.println("\n===== DAILY WATER REPORT =====");
        System.out.println("Total Allocations: " + totalAllocations);
        System.out.println("Total Water Used: " + totalWater + " litres");
    }

    // 4. Weekly water report
    public void generateWeeklyWaterReport(List<Allocation> allocations) {

        LocalDate today = LocalDate.now();

        LocalDate weekStart = today.with(
            WeekFields.ISO.dayOfWeek(), 1
        );

        LocalDate weekEnd = weekStart.plusDays(6);

        System.out.println("\n===== WEEKLY WATER REPORT =====");
        System.out.println("From: " + weekStart);
        System.out.println("To: " + weekEnd);

        Map<LocalDate, Double> dailyUsage = allocations.stream()
            .filter(a -> a.getAllocationDate() != null)
            .filter(a -> !a.getAllocationDate().isBefore(weekStart)
                      && !a.getAllocationDate().isAfter(weekEnd))
            .collect(Collectors.groupingBy(
                Allocation::getAllocationDate,
                TreeMap::new,
                Collectors.summingDouble(Allocation::getAllocatedWater)
            ));

        dailyUsage.forEach((date, water) ->
            System.out.println(date + " : " + water + " litres")
        );

        double weeklyTotal = dailyUsage.values().stream()
            .mapToDouble(Double::doubleValue)
            .sum();

        System.out.println("Weekly Water Consumption: "
                + weeklyTotal + " litres");
    }
}
