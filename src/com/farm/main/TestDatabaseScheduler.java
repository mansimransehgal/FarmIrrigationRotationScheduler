
package com.farm.main;

import java.time.LocalTime;

import com.farm.model.*;
import com.farm.scheduler.IrrigationScheduler;

public class TestDatabaseScheduler {

    public static void main(String[] args) {

        IrrigationScheduler scheduler =
                new IrrigationScheduler();

        // 1. Register a field
        Field field = new Field(
                "F900",
                "Integration Test Field",
                2.0,
                400,
                "HIGH"
        );

        scheduler.registerField(field);

        // 2. Add an irrigation request
        IrrigationRequest request =
                new IrrigationRequest("R900", field, "HIGH");

        scheduler.addRequest(request);

        // 3. Add a time slot
        TimeSlot slot = new TimeSlot(
                "T900",
                LocalTime.of(6, 0),
                LocalTime.of(7, 0)
        );

        scheduler.addTimeSlot(slot);

        // 4. Allocate the time slot
        scheduler.allocateSlots();

        // 5. Display results
        scheduler.displayWaterUsage();
        scheduler.displayAllocations();
    }
}
